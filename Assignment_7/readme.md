

## ✅ STEP-BY-STEP SETUP (WITH COMMANDS)


### 🔹 2. Create and activate a virtual environment

#### On **Linux**:

```bash
python3 -m venv venv
source venv/bin/activate
```

---

### 🔹 3. Install required dependencies

```bash
pip install flask requests
```

---

### 🔹 4. Create files and folders

Create the following structure manually or with commands:

```bash
mkdir templates
touch master.py worker.py templates/dashboard.html
```

Or using code editor (e.g., VS Code), just create:

* `master.py`
* `worker.py`
* `templates/dashboard.html`

---

### 🔹 5. Paste code

#### 🔸 `master.py` (Flask Server)

Paste the full Flask server code with `/get_task`, `/submit_result`, `/status`, and `/` routes, and task list in memory.

#### 🔸 `worker.py` (Worker Threads)

Paste the Python code with multiple `threading.Thread` workers that:

* Fetch from `/get_task`
* Process text
* Submit to `/submit_result`

#### 🔸 `templates/dashboard.html` (Dashboard)

Paste HTML with live updates using:

```js
setInterval(() => fetch('/status')...)
```

---

### 🔹 6. Run the Flask server

```bash
python master.py
```

Then open your browser to:
📍 `http://localhost:5000`

---

### 🔹 7. Run the workers in another terminal

Keep the virtual environment active and run:

```bash
python worker.py
```

Each worker will:

* Get a paragraph
* Count words
* Submit to server
* Log output in terminal
* Trigger dashboard update

---

## 🧠 THEORY + EXPLANATION OF CONCEPTS

### ✅ Flask Basics

* Flask is a **Python web framework** used here as the main server.
* Routes like `/get_task`, `/submit_result`, and `/status` are **RESTful APIs**.
* The `dashboard.html` is served via Flask’s template engine.

### ✅ Multithreading

* Python's `threading.Thread` is used to simulate multiple workers.
* Each thread acts like a separate machine in a distributed system.
* Useful for parallelizing I/O-heavy or CPU-light tasks.

### ✅ Task Distribution

* The master holds a `task_list` in memory — simulating a job queue.
* Each call to `/get_task` returns and removes one item.
* Workers process and submit to `/submit_result`.

### ✅ Real-time Dashboard with AJAX

* JavaScript's `fetch()` calls `/status` every second.
* The browser updates the page without refreshing.
* This creates a **live dashboard experience**.

### ✅ Word Count Logic

* Each worker splits the text using `.split()` and counts the words.
* Simulates a "real" computation task in distributed processing.

---

## 🔁 Example Flow

1. Master has a list of 10 paragraphs.
2. 3 worker threads start.
3. Each thread requests a task → gets a paragraph.
4. Counts words → sends result back.
5. Master stores it → dashboard shows live data.

---

### ✅ Done! You're now running a full multithreaded distributed task simulator with live dashboard updates!