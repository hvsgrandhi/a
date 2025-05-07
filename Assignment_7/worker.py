import requests
import time
import socket
import threading
import random

MASTER_URL = 'http://localhost:5000'
NUM_WORKERS = 5  # You can change this to simulate more/less concurrency

def get_task():
    try:
        res = requests.get(f'{MASTER_URL}/get_task')
        return res.json().get('text')
    except Exception as e:
        print("Error fetching task:", e)
        return None

def submit_result(count, worker_id):
    try:
        res = requests.post(f'{MASTER_URL}/submit_result', json={
            'word_count': count,
            'worker_id': worker_id
        })
        return res.ok
    except Exception as e:
        print("Error submitting result:", e)
        return False

def count_words(text):
    return len(text.split())

def worker_thread(worker_id):
    print(f"🟢 {worker_id} started.")

    while True:
        task = get_task()
        if not task:
            print(f"🔴 {worker_id} found no task. Exiting.")
            break

        print(f"🟡 {worker_id} processing: {task[:50]}...")
        time.sleep(random.uniform(1, 3))  # Simulate processing time
        count = count_words(task)

        if submit_result(count, worker_id):
            print(f"✅ {worker_id} submitted result: {count} words")
        else:
            print(f"❌ {worker_id} failed to submit result")

if __name__ == "__main__":
    threads = []

    print(f"\n🚀 Launching {NUM_WORKERS} parallel worker threads...\n")

    for i in range(NUM_WORKERS):
        wid = f"ThreadWorker-{i+1}"
        t = threading.Thread(target=worker_thread, args=(wid,))
        threads.append(t)
        t.start()

    for t in threads:
        t.join()

    print("\n🎉 All threads completed. Check the dashboard for results.")
