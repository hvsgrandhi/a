from flask import Flask, request, jsonify, render_template
from datetime import datetime
from threading import Lock

app = Flask(__name__)

# Shared task and result queues
tasks = [
    "Lorem ipsum dolor sit amet, consectetur adipiscing elit. " * 10,
    "This is a dummy paragraph that simulates a slightly longer task than before. " * 15,
    "Data preprocessing can be a long task in machine learning pipelines. " * 12,
    "Imagine this being a part of a document analysis system, where each paragraph is parsed and scored. " * 8,
    "The quick brown fox jumps over the lazy dog. " * 25,
    "Distributed systems often require task coordination and consistency maintenance. " * 10,
    "Web scraping large pages or API data aggregation can be chunked and processed in parallel. " * 12,
    "Here’s a really long task. It might simulate PDF parsing, media transcoding, or NLP processing. " * 15,
    "Another heavy paragraph to simulate parallel task execution for performance observation. " * 10,
    "Final simulated workload for testing thread and task coordination under load. " * 10
]

results = []
worker_log = []
task_lock = Lock()

@app.route('/')
def dashboard():
    return render_template("dashboard.html", tasks=tasks, results=results, logs=worker_log)

@app.route('/get_task', methods=['GET'])
def get_task():
    with task_lock:
        if tasks:
            text = tasks.pop(0)
            log = f"[{datetime.now()}] Task assigned: {text[:50]}..."
            print(log)
            worker_log.append(log)
            return jsonify({'text': text})
        else:
            return jsonify({'text': None})

@app.route('/submit_result', methods=['POST'])
def submit_result():
    data = request.json
    word_count = data.get('word_count')
    worker_id = data.get('worker_id')
    with task_lock:
        results.append({
            'worker_id': worker_id,
            'word_count': word_count,
            'timestamp': datetime.now().isoformat()
        })
        worker_log.append(f"Received result from {worker_id}: {word_count} words")
    return jsonify({'status': 'ok'})

@app.route('/status', methods=['GET'])
def get_status():
    return jsonify({
        'tasks_left': len(tasks),
        'results': results,
        'logs': worker_log,
        'total': sum(r['word_count'] for r in results)
    })

if __name__ == '__main__':
    print("🔵 Master server running at http://localhost:5000")
    app.run(port=5000, debug=True)
