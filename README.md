# 💻 Ready to Use Test Automation Framework (TAF)

## 🚀 Introducing the Lean Test Automation Framework (TAF)
The state-of-the-art architecture focuses on **reducing complexity**, **improving maintainability**, and making every layer easily understandable for anyone joining the project.  
In short, it’s built to be **clean**, **scalable**, and **future-ready**.

Because the simpler your test structure, the easier it is to build, debug, and scale.  
👉 So, to make test automation **maintainable, simple, and reusable**, this framework has been designed to match industry-standard practices and can be used for **small projects or start-ups** alike.

---

## ⚙️ Technologies Used

- **Java & Selenium** – Core automation engine  
- **Cucumber (BDD) Native** – Human-readable test scenarios  
- **TestNG** – Test runner + parallel execution  
- **Extent Spark** – Local HTML reports  
- **Allure + GitHub Pages** – Global interactive reports  

---

## 🧩 The 5 Pillars of a Solid TAF

### 1️⃣ Clean Architecture Layering
Clear separation between layers ensures **independence, maintainability, and readability**:
- Build & Dependency Management  
- Configuration Layer  
- Core/Base Classes  
- Test & Page Objects  
- Utility & Execution Layers  

---

### 2️⃣ Design Patterns
Implements key reusable design patterns for flexibility and scalability:
- **Singleton** → Centralized configuration and driver handling  
- **Factory** → Browser and object creation  
- **POM (Page Object Model)** → Reusable page interactions and UI logic isolation  

---

### 3️⃣ Logs & Reports
- Integrated **Extent Spark** for detailed HTML reports  
- **Allure Reports** hosted globally via GitHub Pages  
- Log4j for real-time **exception logging and screenshots** for easy debugging  

---

### 4️⃣ Test Data Management
- Currently, the automation scripts use test data passed from Cucumber feature files (Cucumber Native), which is static in nature.  
- For dynamic data handling, we can expand and integrate formats such as JSON, YAML, CSV, or config-based files to support data-driven testing.​  
- Eliminates hard-coded data for better maintainability  

---

### 5️⃣ Parallel Execution & Scalability
- Run tests concurrently on **local**, **cloud**, or **Selenium Grid** environments  
- Integrates seamlessly with **CI/CD pipelines**  
- Supports scaling via **Dockerized or distributed** runs  

---

## 🧱 Core Components / Building Blocks
(From my handwritten notes 📒 – every layer matters)

| Layer | Description |
|-------|--------------|
| **1️⃣ Core/Base Layer** | Base classes, Driver Factory |
| **2️⃣ Page Object Layer** | Page classes with locators & methods |
| **3️⃣ Test Layer** | Cucumber feature files, step definitions, test classes |
| **4️⃣ Utility Layer** | Wait helpers, loggers, Excel/JSON readers |
| **5️⃣ Reports & Logs Layer** | Extent & Allure integrations |
| **6️⃣ Data Handling Layer** | Data providers, config readers |
| **7️⃣ Parallel Execution Layer** | Thread-safe driver handling |
| **8️⃣ Pipeline & Execution Layer** | CI/CD integrations |
| **9️⃣ Scalability Layer** | Version control hygiene, synchronization, robust waits |

---

## 🧠 Key Features
✅ Follows **DRY**, **KISS**, and **SOLID** principles  
✅ Reusable page objects & centralized configuration  
✅ Cross-browser and cross-platform support  
✅ Parallel test execution with stable synchronization  
✅ Clean version control hygiene & CI/CD readiness  

---

## 🧩 Testing Patterns Implemented
- **Page Object Model (POM)** – for UI abstraction  
- **Factory Pattern** – for object creation  
- **Data-Driven Testing (DDT)** – for dynamic coverage  
- **BDD / TDD / ATDD** – for better collaboration  
- **Observer Pattern (Listeners)** – for event-based reporting  

---

## ⚡ Advantages
✨ Easy to maintain  
✨ Reduces complexity  
✨ Improves readability & reusability  
✨ Highly scalable & CI/CD ready  
✨ Makes testing cleaner, smarter, and faster  

---

## 🧩 Project Structure

```plaintext
selenium/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/automationframework/
│   │   │       ├── core/
│   │   │       ├── enums/
│   │   │       ├── pages/
│   │   │       └── utils/
│   │   └── resources/
│   └── test/
│       ├── java/
│       │   └── com/automationframework/tests/
│       │       ├── listeners/
│       │       ├── runners/
│       │       │   └── CucumberTestRunner.java
│       │       └── stepdefinitions/
│       └── resources/
│           ├── drivers/
│           └── features/
│               ├── HomePage.feature
│               ├── Sanity.feature
│               ├── Smoke.feature
│           ├── extent.properties
│           └── testng.xml
├── JRE System Library [JavaSE-15]
├── Maven Dependencies
├── TestNG/
├── allure-results/
├── logs/
├── reports/
├── src/
├── target/
├── test-output/
└── pom.xml
