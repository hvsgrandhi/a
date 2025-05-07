
# 🧠 Distributed Word Count Dashboard

This project simulates a **distributed system** for word counting using **multithreading, Flask**, and a **live-updating dashboard**. It consists of a `Flask` server acting as the **master** that distributes text processing tasks to multiple **worker threads**. The real-time results are displayed in a web dashboard.


## 📌 Table of Contents
- [Project Overview](#project-overview)
- [Architecture](#architecture)
- [How to Run](#how-to-run)
- [Components](#components)
  - [1. Master Server (Flask)](#1-master-server-flask)
  - [2. Worker Threads](#2-worker-threads)
  - [3. Dashboard UI](#3-dashboard-ui)
- [Key Concepts Explained](#key-concepts-explained)
  - [Multithreading](#multithreading)
  - [Flask REST APIs](#flask-rest-apis)
  - [Task Distribution](#task-distribution)
  - [AJAX & Live Updates](#ajax--live-updates)
- [Output Sample](#output-sample)
- [Use Cases](#use-cases)
- [Conclusion](#conclusion)


## ✅ Project Overview

The system breaks down a text document into multiple paragraphs (tasks), which are sent to worker threads for word counting. Results are submitted back to the server and shown in real-time via a live dashboard.



## 🏗️ Architecture


+------------------+             +------------------+
\|   Worker Thread  |<--pulls---+ |     Flask Server |
\|   (client)       |           | |   (Task Manager) |
+------------------+           | +------------------+
\|                      |         |
+--submits result---->+          |
serves
|
+-----------+
\| Dashboard |
+-----------+



## ▶️ How to Run

### 1. Install dependencies (only `Flask` needed)

pip install flask


### 2. Run the Master Server


python master.py


Go to: [http://localhost:5000](http://localhost:5000) to view the dashboard.

### 3. In another terminal, run Worker Threads


python worker.py


You’ll see output like:


Worker-1: Fetched task from server.
Worker-1: Submitted word count = 56


Dashboard will update live with each worker's results.


## 🔧 Components

### 1. Master Server (Flask)

* Maintains:

  * `tasks`: list of paragraphs to process
  * `results`: list of submitted word counts
  * `worker_log`: tracks worker activity
* Routes:

  * `/` – serves the dashboard HTML
  * `/get_task` – provides next task
  * `/submit_result` – receives word count
  * `/status` – returns real-time data in JSON

### 2. Worker Threads

* Each thread:

  * Pulls a task
  * Counts the number of words
  * Posts the result with worker ID
  * Simulates delay to mimic real computation
* Uses `requests` and `threading` libraries

### 3. Dashboard UI

* Written in HTML + CSS + JS
* Uses `fetch()` every second to update results
* Groups results by worker
* Shows:

  * Word count
  * Worker ID
  * Timestamp


## 📘 Key Concepts Explained

### 💡 Multithreading

* Python’s `threading.Thread` is used to simulate **parallel workers**.
* Each thread acts like a separate worker node in a distributed system.
* Shared console but independently executing threads.

### 💡 Flask REST APIs

* Flask server exposes REST endpoints (`/get_task`, `/submit_result`).
* Workers interact via HTTP requests.
* Server maintains shared state and uses `threading.Lock` to ensure **thread safety**.

### 💡 Task Distribution

* Tasks are simply text paragraphs stored in a list.
* Each worker gets a task, processes it, and reports back.
* Tasks are popped from the list to ensure no duplication.

### 💡 AJAX & Live Updates

* Dashboard polls the server using `fetch('/status')` every second.
* Automatically updates the DOM with latest results.
* Time of submission and worker grouping handled in JavaScript.


## 🧾 Output Sample

### Terminal (Workers):


Worker-1: Submitted word count = 63
Worker-2: Submitted word count = 89
Worker-3: No more tasks left. Exiting.

### Dashboard:

🧑‍💻 Worker-1
- Words: 63 (Submitted at 2025-05-07 12:30:14)
- Words: 71 (Submitted at 2025-05-07 12:30:17)

🧑‍💻 Worker-2
- Words: 89 (Submitted at 2025-05-07 12:30:19)

## 💡 Use Cases

* Learning **distributed system concepts** using simulation.
* Demonstrating **multithreaded task execution**.
* Real-time dashboards for monitoring task completion.
* Educational tool for teaching Flask, REST APIs, threading, and AJAX.

---

## 🏁 Conclusion

This project is a simple but powerful simulation of a **distributed task manager** using core technologies:

* 🐍 Python (multithreading, Flask)
* 📡 REST APIs
* 🌐 Live front-end updates via JavaScript

It can be extended to:

* Add **authentication** for workers
* Track **task execution time**
* Integrate with real job queues like **Celery** or **RabbitMQ**

---

## 📂 File Structure


/project-root
│
├── master.py          # Flask server
├── worker.py          # Simulated worker threads
├── templates/
│   └── dashboard.html # Dashboard UI
└── static/            # (optional) CSS or JS if needed

---