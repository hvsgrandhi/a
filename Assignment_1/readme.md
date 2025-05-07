
## 📁 1. PROJECT STRUCTURE

Your project directory should look like this:

```
YourProject/
│
├── Client/
│   ├── ClientInterface.java
│   ├── ClientImplementation.java
│   └── ClientMain.java
│
├── Server/
│   ├── ServerInterface.java
│   ├── ServerImplementation.java
│   └── ServerMain.java
```

You're working from the **parent folder** of `Client` and `Server`.

---

## 🧠 2. THEORY BEHIND THE APPLICATION

### 🔗 Java RMI (Remote Method Invocation)

* Allows a Java program running on one JVM (client) to call methods on an object in another JVM (server).
* Makes remote calls look like **local method calls** using **stubs** and **remote interfaces**.

---

### 🧱 RMI Key Components

| Component            | Role                                                                                           |
| -------------------- | ---------------------------------------------------------------------------------------------- |
| **Remote Interface** | Declares the methods that can be called remotely (e.g., `ServerInterface`, `ClientInterface`). |
| **Stub**             | Proxy object that forwards client method calls to the remote server object.                    |
| **Remote Object**    | The actual object on the server or client that implements the interface.                       |
| **RMI Registry**     | Acts like a directory for binding and looking up remote objects using a name.                  |

---

### 🔁 Flow Summary

1. **Server** starts and registers itself with the RMI registry.
2. **Client** looks up the server from the registry and gets its **stub**.
3. **Client registers itself** (sends its own stub to the server).
4. **Client inputs numbers** and sends to the server.
5. **Server broadcasts** those numbers to all registered clients using their stubs.
6. Each client **sorts and displays** the list.

---

## 🛠️ 3. STEP-BY-STEP INSTRUCTIONS (WITH TERMINALS)

---

### ✅ Terminal 1 – Compile & Start RMI Registry

> Stay in the **parent folder** of `Client/` and `Server/`

```bash
javac Client/*.java
javac Server/*.java
rmiregistry 5000
```

🔍 **Explanation**:

* `javac Client/*.java`: Compiles all client files.
* `javac Server/*.java`: Compiles all server files.
* `rmiregistry 5000`: Starts RMI registry on port `5000`.

  * This registry holds the **bound server object** so clients can discover it.

> Keep this terminal running. Don’t close it.

---

### ✅ Terminal 2 – Start the Server

```bash
java Server.ServerMain
```

🔍 **Explanation**:

* Runs `ServerMain.java`.
* Starts server instance and **registers/binds** it in the RMI registry:

  ```java
  LocateRegistry.createRegistry(5000);
  Naming.rebind("rmi://localhost:5000/Server", server);
  ```

> Now the server is **discoverable** by name `"Server"` on port 5000.

---

### ✅ Terminal 3 – Start the Client

```bash
java Client.ClientMain
```

🔍 **Explanation**:

* Client does:

  1. **Looks up** the server:

     ```java
     ServerInterface server = (ServerInterface) Naming.lookup("rmi://localhost:5000/Server");
     ```
  2. **Creates its stub** and sends it to the server:

     ```java
     server.registerClient(client);
     ```
  3. **Reads a list of integers** from user input.
  4. **Calls `broadcastNumbers()`** on server to send the list.

---

## 📤 BROADCAST MECHANISM – Final Step

When the server receives the list:

```java
for (ClientInterface client : clients) {
    client.sortAndDisplay(numbers);
}
```

* The server **calls each client's `sortAndDisplay()`** using their stubs.
* Each client sorts the list and prints it.

---

## ✅ FINAL OVERVIEW

| Step | Component        | Description                                               |
| ---- | ---------------- | --------------------------------------------------------- |
| 1    | Compile          | Compiles all Java files.                                  |
| 2    | RMI Registry     | Starts directory service on port 5000.                    |
| 3    | Server Start     | Registers the server object in the registry.              |
| 4    | Client Start     | Client gets server stub, registers itself, sends numbers. |
| 5    | Server Broadcast | Server uses client stubs to send numbers back.            |
| 6    | Client Callback  | Clients sort and display numbers.                         |

---
