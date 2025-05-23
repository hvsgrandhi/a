## 💡 What is CORBA?

**CORBA (Common Object Request Broker Architecture)** is a **middleware standard** defined by **OMG (Object Management Group)** that allows communication between **objects written in different programming languages** and running on different machines.

* It enables distributed computing by acting as a **broker** between client and server objects.
* Java supports CORBA through **Java IDL (Interface Definition Language)**.
* ORB (Object Request Broker) is the core of CORBA, which allows clients to invoke methods on a remote object as if it's local.

---

## 📁 Folder Structure

```plaintext
corba-calculator/
│
├── Calculator.idl           # Interface Definition
├── CalculatorImpl.java      # Implementation of the interface (Server Side)
├── CalculatorServer.java    # Server setup code
├── CalculatorClient.java    # Client code to invoke methods
```

---

## 🧩 Files Explained

### `Calculator.idl`

Defines the **remote interface** that both client and server understand.

```idl
module CalculatorApp {
  interface Calculator {
    float add(in float a, in float b);
    float subtract(in float a, in float b);
    float multiply(in float a, in float b);
    float divide(in float a, in float b);
  };
};
```

* `module CalculatorApp` creates a namespace.
* `interface Calculator` defines remote methods with parameters marked as `in`.

---

### `CalculatorImpl.java`

This class implements the remote interface.

```java
public class CalculatorImpl extends CalculatorPOA {
    public float add(float a, float b) { return a + b; }
    public float subtract(float a, float b) { return a - b; }
    public float multiply(float a, float b) { return a * b; }
    public float divide(float a, float b) { return b == 0 ? 0 : a / b; }
}
```

* Inherits from `CalculatorPOA` (generated by `idlj` compiler).
* Implements actual logic of the methods.

---

### `CalculatorServer.java`

Sets up the server to host the Calculator object.

```java
public class CalculatorServer {
    public static void main(String[] args) {
        ...
        ORB orb = ORB.init(args, null);
        POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
        rootpoa.the_POAManager().activate();

        CalculatorImpl calcImpl = new CalculatorImpl();
        org.omg.CORBA.Object ref = rootpoa.servant_to_reference(calcImpl);
        Calculator href = CalculatorHelper.narrow(ref);

        NamingContextExt ncRef = NamingContextExtHelper.narrow(
            orb.resolve_initial_references("NameService")
        );

        NameComponent path[] = ncRef.to_name("Calculator");
        ncRef.rebind(path, href);

        System.out.println("Calculator Server ready and waiting...");
        orb.run();
        ...
    }
}
```

* Registers the implementation with the **naming service**.

---

### `CalculatorClient.java`

Client that looks up the server and calls methods.

```java
public class CalculatorClient {
    public static void main(String[] args) {
        ...
        ORB orb = ORB.init(args, null);
        NamingContextExt ncRef = NamingContextExtHelper.narrow(
            orb.resolve_initial_references("NameService")
        );
        Calculator calcRef = CalculatorHelper.narrow(ncRef.resolve_str("Calculator"));
        
        // Remote method calls
        System.out.println("Addition: " + calcRef.add(10, 5));
        ...
    }
}
```

---

## 🛠️ Practical Setup (Step-by-Step)

### 🧱 Step 1: Install Java

```bash
sudo apt update
sudo apt install openjdk-8-jdk -y
java -version
```

Make sure version is 1.8 (CORBA removed in Java 11+).

### ✅ Step 2: Configure Java (if multiple versions)

```bash
sudo update-alternatives --config java
```

```bash
sudo update-alternatives --config javac
```

Choose Java and javac 8.

---

### 📌 Step 3: Create and Compile IDL File

```bash
idlj -fall Calculator.idl
```

This generates a `CalculatorApp/` folder containing:

* `Calculator.java` (interface)
* `CalculatorHelper.java`, `CalculatorHolder.java`
* `CalculatorOperations.java`
* `CalculatorPOA.java` (Server-side base class)

---

### 🧾 Step 4: Compile Java Code

```bash
javac *.java CalculatorApp/*.java
```

This compiles:

* Your implementation classes
* The generated IDL stubs/skeletons

---

### 🌐 Step 5: Start ORB Daemon

Start the **CORBA Naming Service (ORBD)**:

```bash
orbd -ORBInitialPort 1050
```

> Keep this terminal open. It allows naming service binding/lookup.

---

### 🖥️ Step 6: Run Server

```bash
java CalculatorServer -ORBInitialPort 1050 -ORBInitialHost localhost
```

You should see:

```
Calculator Server ready and waiting...
```

---

### 🧪 Step 7: Run Client

In a new terminal:

```bash
java CalculatorClient -ORBInitialPort 1050 -ORBInitialHost localhost
```

Expected output:

```
Addition: 15.0
Subtraction: 5.0
Multiplication: 50.0
Division: 2.0
```

---

## 🔬 Theory Summary

### 1. IDL (Interface Definition Language)

* Language-independent way to define object interfaces.
* Translated into Java using `idlj`.

### 2. ORB (Object Request Broker)

* Middleware that handles communication between client and server.
* Client uses **stubs**, server uses **skeletons** generated from IDL.

### 3. Naming Service

* Acts like a **phonebook** to locate objects by name.
* `orbd` runs naming service on a known port (e.g., 1050).
* Used to bind (`rebind`) and resolve object references.

### 4. Servant Object

* Actual implementation of remote interface on the server side (`CalculatorImpl`).

---

## 📘 Optional Concepts to Explore

| Concept                       | Explanation                                                                |
| ----------------------------- | -------------------------------------------------------------------------- |
| POA (Portable Object Adapter) | Manages object lifecycles and request dispatching.                         |
| Helper & Holder classes       | Auto-generated by `idlj`. Help in narrowing references and passing values. |
| CORBA vs RMI                  | CORBA is language-neutral, RMI is Java-only.                               |
| Java 11+                      | CORBA support removed. Use Java 8 or third-party libraries.                |

---











# Theory
Absolutely. Let's walk through **everything from concept to code to commands**, in a **detailed and clear manner**, starting from the very beginning.

---

## 🌐 What is CORBA?

CORBA stands for **Common Object Request Broker Architecture**. It’s a **middleware** technology developed by the **Object Management Group (OMG)** that allows pieces of programs written in different languages and running on different computers to work together — i.e., it allows **distributed applications**.

It works on the **client-server model**, where the client requests services (like adding two numbers), and the server provides those services — across the network.

---

## 📂 Files and Their Purpose

### 1. `Calculator.idl` – **Interface Definition Language**

This file describes the **interface** of the remote object (here: `Calculator`) in **language-independent format**.

```idl
module CalculatorApp {
  interface Calculator {
    float add(in float a, in float b);
    float subtract(in float a, in float b);
    float multiply(in float a, in float b);
    float divide(in float a, in float b);
  };
};
```

#### Key Concepts:

* **IDL** allows defining interfaces that can be used across different programming languages.
* `module CalculatorApp`: Namespacing like Java packages.
* `interface Calculator`: Declares four operations.
* `in float a, in float b`: These are input parameters (no `out` or `inout` here).

---

## 🔄 Code Flow Explanation (Step-by-Step)

### 🔧 Step 1: Compile IDL → Generate Stubs and Skeletons

```sh
idlj -fall Calculator.idl
```

* `idlj`: The Java IDL compiler.
* `-fall`: Generate all files needed (`f` for all files, `a` for abstract base classes, `l` for implementation templates).

➡️ This creates:

```
CalculatorApp/
├── Calculator.java              (Interface)
├── CalculatorHelper.java       (Helper class for narrowing and working with remote objects)
├── CalculatorHolder.java       (For inout/out parameters, not used here)
├── CalculatorOperations.java   (Actual interface methods)
├── CalculatorPOA.java          (Server-side abstract base class - your server extends this)
```

---

### 👨‍💻 `CalculatorImpl.java` – **Server Implementation**

This is the **server-side implementation** of the remote interface.

```java
public class CalculatorImpl extends CalculatorPOA {
    // Implements each method
}
```

* `CalculatorPOA` is the **skeleton** class generated by `idlj`, and your class implements the logic.
* This is where actual computation happens.
* Includes **logging** for understanding client interactions.

---

### 🖥️ `CalculatorServer.java` – **CORBA Server**

Responsible for:

1. Initializing ORB.
2. Registering the object with the **CORBA Naming Service**.
3. Starting the server loop (`orb.run()`).

```java
ORB orb = ORB.init(args, null);
POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
rootpoa.the_POAManager().activate();
```

* **ORB (Object Request Broker)**: Core of CORBA. Manages communication.
* **POA (Portable Object Adapter)**: Bridges ORB and servant (implementation object).
* `rootpoa.servant_to_reference(...)`: Converts your servant to a CORBA object reference.
* `NamingContextExt`: CORBA's directory service — binds the object with a name (`"Calculator"`) that clients can look up.

```java
orb.run();
```

➡️ Keeps the server running and **listening for client requests**.

---

### 👤 `CalculatorClient.java` – **CORBA Client**

This is the program that:

1. Starts an ORB.
2. Looks up the `"Calculator"` object in the naming service.
3. Calls methods on it remotely (which are actually run on the server).

```java
ORB orb = ORB.init(args, null);
...
Calculator calcRef = CalculatorHelper.narrow(ncRef.resolve_str("Calculator"));
```

* `resolve_str("Calculator")`: Fetches reference bound with name `"Calculator"`.
* `CalculatorHelper.narrow(...)`: Typecast to correct interface type.
* Then uses `calcRef.add(...)`, `subtract(...)`, etc.

These calls go over the **network using CORBA**, and the server executes the logic.

---

## 💻 Linux Commands – Setup and Execution

Now let’s understand every terminal command step-by-step.

### 🔧 1. Java Setup

```sh
sudo apt update
sudo apt install openjdk-8-jdk -y
java -version
```

* Installs Java 8 JDK, necessary for running Java + CORBA code.
* CORBA in Java is deprecated in later versions (like Java 11+), so Java 8 is used here.
* `java -version` confirms the installation.

### 🔀 2. Choose Java Version (if multiple)

```sh
sudo update-alternatives --config java
sudo update-alternatives --config javac
```

Allows you to switch between multiple installed Java versions (e.g., Java 11 and Java 8).

---

### 🛠️ 3. Compile the IDL File

```sh
idlj -fall Calculator.idl
```

* Generates necessary CORBA Java code in `CalculatorApp/`.

---

### 📦 4. Compile Java Files

```sh
javac *.java CalculatorApp/*.java
```

* Compiles all `.java` files, including those in the `CalculatorApp/` folder.

---

### 📡 5. Start the ORB Daemon (CORBA naming service)

```sh
orbd -ORBInitialPort 1050
```

* Starts the **Object Request Broker Daemon**, needed to manage object references and directory.
* Port `1050` is used for both client and server to communicate with ORB.

> Leave this running in one terminal.

---

### 🚀 6. Start the Server

```sh
java CalculatorServer -ORBInitialPort 1050 -ORBInitialHost localhost
```

* Binds `"Calculator"` object to naming service on port 1050.

> Keep this running in a second terminal.

---

### 📞 7. Run the Client

```sh
java CalculatorClient -ORBInitialPort 1050 -ORBInitialHost localhost
```

* Connects to the naming service.
* Looks up `"Calculator"` object.
* Calls remote methods → logs are shown in both client and server.

---

## ✅ Summary of Flow

### ➤ Compilation & Setup

1. Define interface in `.idl`.
2. Generate code using `idlj`.
3. Implement server-side logic.
4. Compile everything.

### ➤ Execution

1. Start `orbd` (Naming service).
2. Run `CalculatorServer` – binds the service.
3. Run `CalculatorClient` – performs remote method calls.

---

## 🔄 Request Flow

```text
Client → ORB → NamingService → Gets Object Reference
        ↓
   Calls add(10, 5) on remote Calculator
        ↓
Server → Performs addition → returns result to client
```

* All method calls are transparently made to the server — CORBA takes care of the communication.

---

Would you like me to provide a folder structure layout, or explain stubs, skeletons, or ORB internals next?

You're asking some great foundational questions about CORBA (Common Object Request Broker Architecture). Let's break each one down with clear explanations and analogies.

---

### 🧩 1. **What are Stubs and Skeletons?**

These are **auto-generated code** by CORBA tools that help client and server communicate over the network.

#### 🔧 Technical Definition:

* **Stub** (Client-side): Acts as a **proxy** for the remote object. It appears like the real object to the client.
* **Skeleton** (Server-side): Receives calls from the ORB and forwards them to the **servant (your implementation)**.

#### 🎭 Analogy:

Imagine a restaurant:

* **Stub** = Waiter taking your order (Client makes a request)
* **Skeleton** = Kitchen receiver who gives your order to the chef (Server processes the request)
* **Servant** = The Chef who actually prepares your food (The real implementation logic)

So, the client talks to the waiter (Stub), who passes the request to the kitchen (Skeleton), and the chef (Servant) does the real work.

---

### 🧠 2. **What is ORB (Object Request Broker)?**

ORB is the **middleware** that handles communication between client and server in CORBA.

#### 📌 Responsibilities:

* Locating the server object.
* Sending method calls from client to server.
* Returning results back to the client.

#### 🎭 Analogy:

Think of the **ORB as a telephone exchange**. You dial a number (make a request), the exchange finds the correct person and connects you (routes the call).

---

### 📛 3. **What is the Naming Service in CORBA?**

The **Naming Service** is like a **phonebook or DNS** for CORBA. It maps **names to object references**.

#### Example:

You register your server object with a name like `"Calculator"` and the client uses that name to find the object.

---

### 🔎 4. **What is CORBA Naming Service?**

It's a CORBA component that allows:

* **Server** to bind object references with names.
* **Clients** to look up these names to get the reference.

This avoids the need for clients to know the exact memory reference or address of the object. It’s like asking, *“Who is ‘Calculator’?”* and the naming service returns the object reference.

---

### 📦 5. **What exactly does `CalculatorPOA` contain?**

This is the **abstract class** generated from the IDL file by `idlj`. It extends `Servant` and defines:

* Abstract methods like `add()`, `subtract()`, etc.
* Logic to receive remote calls from ORB and forward to your implementation.

> You **extend `CalculatorPOA` in your `CalculatorImpl`** to provide the actual functionality.

---

### 👨‍🍳 6. **What is a Servant (Implementation Object)?**

Servant = The actual **implementation** of the CORBA interface on the server.

In your case:

```java
public class CalculatorImpl extends CalculatorPOA
```

* This class implements all the methods like `add`, `subtract`, etc.
* When the client makes a call, it eventually reaches this class.

#### 🎭 Analogy:

If the Skeleton is the kitchen receiver, **Servant is the chef** who actually prepares the order.

---

Would you like a diagram to visualize this flow between client, ORB, naming service, and server?


Excellent line of thought — let’s break this down **from first principles** and **build up with analogy and depth** so that every term makes sense.

---

## 🧱 What is an Object?

In **Object-Oriented Programming**, an object is an **instance** of a class.
It bundles:

* **Data** (attributes/state)
* **Methods** (functions/behavior)

### 🧠 In CORBA:

An **object** refers to something you can **invoke methods on remotely** — like a service.

---

## 🔌 What is a Server Object in CORBA?

A **server object** is:

* An object (instance of a class) created on the **server-side**
* Implements the **IDL-defined interface**
* **Registered** with the ORB and **exposed to clients**
* **Receives remote method calls** through the CORBA infrastructure

In your code, this is:

```java
CalculatorImpl calcImpl = new CalculatorImpl();  // This is the server object
```

It implements:

```java
public class CalculatorImpl extends CalculatorPOA
```

This object is then **converted into a CORBA object reference** using:

```java
org.omg.CORBA.Object ref = rootpoa.servant_to_reference(calcImpl);
```

That CORBA object is what the client can remotely invoke.

---

## 🎭 Analogy: Object and Server Object

### 🧰 Think of a real-world calculator:

* A **Calculator** is an object → it has buttons (methods) and a display (state).
* If you **own the calculator**, you can press the buttons directly → local method call.

### 🔁 In CORBA:

* You can use someone else's calculator remotely → you call a **stub**.
* The **actual calculator sitting in the other room is the server object**.
* You press a button → message travels → calculator processes → result returns.

So the **server object** is the **actual calculator in the server room** that receives your request and performs the operation.

---

## 📛 Why Do We Bind the Object as `"Calculator"` in Naming Service?

Because just like **humans use names**, clients don't know internal memory addresses — they use a **logical name** to find the service.

This is done with:

```java
ncRef.rebind(path, href);  // href = object reference
```

* `"Calculator"` is just the name used to **register the server object**.
* It’s like saying, *"Hey Naming Service, if someone asks for 'Calculator', give them this object reference."*

---

## 📦 Is This Similar to RMI Registry?

✅ YES, it's **very similar** in spirit.

### CORBA Naming Service vs RMI Registry:

| Feature                | CORBA Naming Service                        | Java RMI Registry                     |
| ---------------------- | ------------------------------------------- | ------------------------------------- |
| Purpose                | Bind names to object references             | Bind names to remote objects          |
| Language support       | Language-independent (Java, C++, Python...) | Java-only                             |
| Communication protocol | IIOP (Internet Inter-ORB Protocol)          | Java RMI over TCP                     |
| Registration           | `rebind("Calculator", object)`              | `Naming.rebind("Calculator", object)` |
| Lookup                 | `resolve_str("Calculator")`                 | `Naming.lookup("Calculator")`         |

So yes, the **Naming Service in CORBA is conceptually the same** as the **RMI Registry** — it maps a name to a remote object reference.

---

## 🧵 Final Recap

* An **object** = logical unit with data and behavior.
* A **server object** = an object created on the server, that implements a remote interface, and is registered with the ORB.
* We **register it with a name** (`"Calculator"`) in the **Naming Service** so clients can find it.
* This **Naming Service is like RMI Registry**, but CORBA is language-neutral and more general.

Would you like me to explain **POA (Portable Object Adapter)** next? It’s the key bridge between servant and ORB.


Yes, you're absolutely right — the current CORBA client code directly uses hardcoded values like `10` and `5` for addition, subtraction, etc.
✅ **You *can* modify it to take user input** using Java’s `Scanner` class (or any input mechanism), and then pass those values to the remote CORBA methods.

---

## 📌 Let’s Start with the Existing `CalculatorClient.java`

Here’s your original version:

```java
System.out.println("Addition: " + calcRef.add(10, 5));
```

We can change this to:

```java
Scanner sc = new Scanner(System.in);
System.out.print("Enter two numbers: ");
float a = sc.nextFloat();
float b = sc.nextFloat();
System.out.println("Addition: " + calcRef.add(a, b));
```

I’ll show you the **updated version** after a full explanation. Now let’s go **line-by-line** through both **client** and **server** code for deep understanding.

---

## 🧑‍💻 Line-by-Line: `CalculatorClient.java` (Client)

```java
import CalculatorApp.*;
import org.omg.CORBA.*;
import org.omg.CosNaming.*;
```

* `CalculatorApp.*` – this is the package generated by the IDL compiler (`idlj`) from `Calculator.idl`.
* `org.omg.CORBA.*` – base CORBA classes: `ORB`, `Object`, etc.
* `org.omg.CosNaming.*` – gives access to CORBA Naming Service (like RMI registry).

---

```java
ORB orb = ORB.init(args, null);
```

* **Initialize the ORB (Object Request Broker)**.
* The ORB connects your client to the server.
* `args` can contain host and port info (`-ORBInitialHost`, `-ORBInitialPort`).

---

```java
org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
```

* Ask the ORB for access to **Naming Service**.
* `narrow()` downcast from generic `Object` to specific `NamingContextExt`, so you can search by name (`resolve_str()`).

---

```java
Calculator calcRef = CalculatorHelper.narrow(ncRef.resolve_str("Calculator"));
```

* You ask the Naming Service: "Give me the object registered as 'Calculator'."
* `narrow()` casts it into a usable `Calculator` interface.

---

```java
System.out.println("Addition: " + calcRef.add(10, 5));
```

* Call a remote method. The stub sends this request to the server object.
* The server does the calculation and sends the result back.

---

### ✅ Modified Client Code with User Input

Here’s a full version with user input:

```java
import CalculatorApp.*;
import org.omg.CORBA.*;
import org.omg.CosNaming.*;
import java.util.Scanner;

public class CalculatorClient {
    public static void main(String[] args) {
        try {
            ORB orb = ORB.init(args, null);
            org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
            Calculator calcRef = CalculatorHelper.narrow(ncRef.resolve_str("Calculator"));

            Scanner sc = new Scanner(System.in);
            System.out.print("Enter first number: ");
            float a = sc.nextFloat();
            System.out.print("Enter second number: ");
            float b = sc.nextFloat();

            System.out.println("Addition: " + calcRef.add(a, b));
            System.out.println("Subtraction: " + calcRef.subtract(a, b));
            System.out.println("Multiplication: " + calcRef.multiply(a, b));
            System.out.println("Division: " + calcRef.divide(a, b));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

---

## 🧑‍💻 Line-by-Line: `CalculatorServer.java` (Server)

```java
import CalculatorApp.*;
import org.omg.CORBA.*;
import org.omg.PortableServer.*;
import org.omg.CosNaming.*;
```

* All needed imports: `CalculatorApp.*` for interfaces and POA, CORBA base, and naming utilities.

---

```java
ORB orb = ORB.init(args, null);
```

* Start the server-side ORB.
* This ORB will now accept requests.

---

```java
POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
rootpoa.the_POAManager().activate();
```

* Get reference to **POA** (Portable Object Adapter).
* POA bridges your **servant object** to CORBA object references.
* Activate POA manager to start listening for client invocations.

---

```java
CalculatorImpl calcImpl = new CalculatorImpl();
```

* Create your **servant object** — the actual implementation of the Calculator methods.

---

```java
org.omg.CORBA.Object ref = rootpoa.servant_to_reference(calcImpl);
Calculator href = CalculatorHelper.narrow(ref);
```

* Convert the local servant into a CORBA object reference.
* `href` can now be registered with the Naming Service.

---

```java
org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
```

* Access the Naming Service again.

---

```java
NameComponent path[] = ncRef.to_name("Calculator");
ncRef.rebind(path, href);
```

* Bind the name `"Calculator"` to the object reference `href`.
* Now clients can look it up by name.

---

```java
System.out.println("[Server] Calculator Server is ready...");
orb.run();
```

* `orb.run()` is a **blocking call** — the server **waits forever** for client requests.
* It’s like opening the gates and saying, “Clients may now connect.”

---

### 🔁 What Happens When Client Calls `calcRef.add(a, b)`?

1. Client calls `calcRef.add(a, b)`
2. Stub sends request over the network to the server
3. ORB on server receives it
4. POA dispatches it to `CalculatorImpl.add(a, b)`
5. Method runs, returns result
6. ORB sends result back to client

---

Would you like a **diagram of the flow between client ↔ ORB ↔ POA ↔ servant ↔ ORB ↔ client**?


Great! Let's clarify all those terms and then walk through the **CORBA command sequence** line by line.

---

## 🧠 PART 1: Understanding Key CORBA Terms

### 🔹 1. **POA (Portable Object Adapter)**

* **What is it?**
  A bridge between your servant (implementation object) and the ORB.
* **Analogy:**
  Think of POA as a receptionist who accepts client requests and knows how to forward them to the right employee (the servant).

---

### 🔹 2. **Servant (Implementation Object)**

* **What is it?**
  This is your actual object that implements the logic for the remote methods (e.g., `add`, `subtract`).
* **Example:**
  `CalculatorImpl extends CalculatorPOA` — this is your servant.
* **Analogy:**
  The actual employee in the office who does the job requested by the client.

---

### 🔹 3. **CORBA Object**

* **What is it?**
  A network-accessible object reference created by the POA using the servant.
* **This is what the client gets when it resolves the name `Calculator`.**
* **Analogy:**
  A phone number through which the client can reach the employee (servant) — they don’t know the employee directly but can reach them via this object reference.

---

### 🔹 4. **POA Manager**

* **What is it?**
  It controls when the POA starts processing incoming requests.
* **Command:**
  `rootpoa.the_POAManager().activate();`
* **Analogy:**
  Like turning on the receptionist’s computer — now they can begin accepting and routing calls.

---

---

## 🧪 PART 2: Commands to Compile and Run a CORBA Program

Let's assume your files are:

* `Calculator.idl`
* `CalculatorImpl.java`
* `CalculatorServer.java`
* `CalculatorClient.java`

---

### 📄 Step-by-step Commands

---

### ✅ Step 1: Compile the IDL File

```bash
idlj -fall Calculator.idl
```

**Explanation:**

* `idlj` = IDL-to-Java compiler.
* `-fall` = generate all files (including stubs, skeletons, POA-based).
* This creates `CalculatorApp` folder with:

  * `Calculator.java` (interface)
  * `CalculatorHelper.java` (helps in narrowing references)
  * `CalculatorPOA.java` (servant base class)
  * `CalculatorOperations.java`, etc.

---

### ✅ Step 2: Compile All Java Files

```bash
javac CalculatorApp/*.java
javac CalculatorImpl.java CalculatorServer.java CalculatorClient.java
```

**Explanation:**

* First, compile the auto-generated CORBA files inside `CalculatorApp`.
* Then compile your own files.

---

### ✅ Step 3: Start the CORBA Naming Service

```bash
tnameserv -ORBInitialPort 1050 &
```

**Explanation:**

* `tnameserv` starts the CORBA Naming Service (like RMI Registry).
* `-ORBInitialPort 1050` means it will run on port 1050.
* `&` (Linux) runs it in background. (Use `start` on Windows or open new terminal.)

---

### ✅ Step 4: Start the Server

```bash
java CalculatorServer -ORBInitialPort 1050 -ORBInitialHost localhost
```

**Explanation:**

* This runs your CORBA server.
* It connects to the Naming Service at `localhost:1050`.
* The server binds the `Calculator` name and waits for client requests.

---

### ✅ Step 5: Start the Client

```bash
java CalculatorClient -ORBInitialPort 1050 -ORBInitialHost localhost
```

**Explanation:**

* The client contacts the naming service at port 1050 on localhost.
* It looks up `"Calculator"`, gets the object reference, and makes method calls.

---

## 🧩 Bonus Summary Analogy:

| CORBA Concept  | Real-World Analogy                        |
| -------------- | ----------------------------------------- |
| ORB            | Post office                               |
| POA            | Reception desk                            |
| Servant        | Employee doing the task                   |
| CORBA Object   | Contact number to reach employee          |
| POA Manager    | Switch to turn on receptionist            |
| Naming Service | Company directory                         |
| `tnameserv`    | Directory services guy managing that list |

---

Would you like me to give you a shell script or `.bat` file to automate all the compilation and execution steps?
