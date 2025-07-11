# E-commerce API Automation Framework

A robust API automation framework for an E-commerce platform, built using Behavior-Driven Development (BDD) principles with Cucumber, leveraging RestAssured for seamless API interactions, and TestNG for powerful test execution and parallelization.

## 🌟 Features

This framework automates key E-commerce functionalities at the API level:

- **User Authentication**: Secure login functionality
- **Product Management**: Creating new products
- **Order Management**: Creating new orders and retrieving order details
- **Product Deletion**: Removing existing products
- **Parallel Execution**: Designed for efficient and reliable concurrent test runs

## 🛠️ Technologies Used

- **Language**: Java Development Kit (JDK) 11+
- **Build Tool**: Apache Maven 3.6.0+
- **API Automation**: RestAssured
- **BDD Framework**: Cucumber-JVM
- **Test Runner**: TestNG (integrated with Cucumber for parallel execution)
- **JSON Processing**: Jackson (for POJO serialization/deserialization, implicitly used by RestAssured)
- **Assertions**: JUnit Assertions (used within step definitions)

## 📂 Project Structure

The project follows a standard Maven directory structure, organized for clarity and maintainability:

```
APIFramework/
├── .gitignore                                        # Specifies intentionally untracked files to ignore
├── pom.xml                                           # Maven Project Object Model file
├── src/
│   ├── main/
│   │   └── java/
│   │       ├── ECommerce_pojo/                       # POJOs for API request/response bodies
│   │       │   ├── LoginRequest.java
│   │       │   ├── LoginResponse.java
│   │       │   └── ... (Other POJOs like Orders, OrdersRequestMain etc.)
│   │       └── Utilities/                            # Reusable utility classes and API constants
│   │           ├── APIResource.java                  # Enum for API endpoints
│   │           ├── ConfigReader.java                 # Handles reading from config.properties
│   │           ├── RequestSpecificationUtil.java     # Builds RestAssured RequestSpecification
│   │           └── ... (Other utilities like JsonPathUtil, TestContext)
│   └── test/
│       ├── java/
│       │   ├── Features/                            # Gherkin Feature Files
│       │   │   └── PlaceOrder.feature
│       │   ├── runner/                              # TestNG Cucumber Test Runner
│       │   │   └── TestRunner.java
│       │   └── stepDefinitions/                     # Cucumber Step Definition classes and Hooks
│       │       ├── Hooks.java
│       │       └── PlaceOrder_StepDef.java
│       └── resources/                               # Configuration and test data files
│           └── config.properties.example            # Template for sensitive configuration
├── testng.xml                                       # TestNG suite configuration for parallel execution
└── README.md                                        # This file
```

## 🚀 Getting Started

Follow these steps to set up and run the project on your local machine.

### Prerequisites

- **Java Development Kit (JDK) 11 or higher**: [Download JDK](https://www.oracle.com/java/technologies/downloads/)
- **Apache Maven 3.6.0 or higher**: [Download Maven](https://maven.apache.org/download.cgi)
- **Eclipse IDE**: (or IntelliJ IDEA, VS Code with Java extensions)

### 1. Clone the Repository

```bash
git clone https://github.com/DeepikaJpr/APIFramework.git
cd APIFramework
```

### 2. Configure Sensitive Information

This project uses a `config.properties` file for sensitive data like login credentials. This file is ignored by Git for security reasons.

1. Navigate to `src/test/resources/`
2. You will find a file named `config.properties.example`
3. Copy `config.properties.example` and rename the copy to `config.properties` in the same directory
4. Open `config.properties` and update the placeholder values with your actual E-commerce API login credentials and base URL:

```properties
# config.properties (DO NOT COMMIT THIS FILE!)
baseUrl=https://your-ecommerce-api.com
ecomLogin=your_username@example.com
ecomPassword=your_secure_password
# Add any other sensitive API keys or URLs here
```

> **Important**: Ensure `config.properties` remains in your local `.gitignore` and is never committed to the repository.

### 3. Import into IDE (Eclipse Example)

1. Open Eclipse
2. Go to **File > Import...**
3. Select **Maven > Existing Maven Projects** and click **Next**
4. Browse to the root directory of your cloned project (`APIFramework`)
5. Click **Finish**. Maven will download all necessary dependencies

## 🏃 Running Tests

Tests can be executed via Maven commands (recommended for CI/CD) or directly from your IDE.

### Via Maven (Recommended for CI/CD)

Maven's test phase will execute tests configured in `testng.xml` using its default test execution plugin (Surefire).

**Build and Run All Tests:**
```bash
mvn clean install
```

This command will clean the project, compile the code, and execute all tests defined in the `testng.xml`.

**Run Tests using testng.xml explicitly:**
```bash
mvn test -Dsurefire.suiteXmlFiles=TestNG.xml
```

**Run Specific Scenarios using Cucumber Tags:**

You can use Cucumber tags defined in your feature files to run a subset of scenarios.

For example, to run only scenarios tagged with `@CreateProduct`:
```bash
mvn test -DCucumber.options="--tags @CreateProduct"
```

To run scenarios tagged with both `@DeleteProduct` and `@CreateProduct`:
```bash
mvn test -DCucumber.options="--tags '@DeleteProduct and @CreateProduct'"
```

### Via Eclipse IDE

**Run TestRunner.java:**
1. Navigate to `src/test/java/runners/TestRunner.java`
2. Right-click on the file > **Run As > TestNG Test**
3. This will execute all feature files configured in `TestRunner.java` using TestNG's runner

## ⚡ Parallel Execution

This framework is designed for efficient parallel test execution, significantly reducing the overall test suite runtime.

- **Configuration**: Parallel execution is configured in `testng.xml` with `parallel="methods"` and `thread-count` (e.g., 4 threads)
- **Mechanism**: TestNG distributes individual Cucumber scenarios (which are treated as `@Test` methods by `AbstractTestNGCucumberTests`) across multiple threads
- **Test Isolation**: Each scenario is designed to be self-contained and independent. If a scenario (e.g., "Delete Product") requires prerequisites like user login or product creation, these steps are performed internally within that scenario's dedicated thread using `@Before` hooks and a `ScenarioContext`. This ensures no interference or data collision between concurrently running tests

For example, if "Delete Product" runs on Thread A, it will perform its own login and create its own unique product on Thread A, then delete that specific product. It will not rely on a login or product created by a "Create Product" scenario running on Thread B.

## 📊 Reporting

After test execution, detailed HTML and JSON reports are generated in the `target/` directory:

- **Surefire Report**: `target/surefire-reports/index.html` (For integration with other reporting tools)

## 🎨 Design Principles & Best Practices

- **Behavior-Driven Development (BDD)**: Scenarios are written in Gherkin (Given-When-Then) to promote collaboration and provide clear, executable specifications
- **Test Isolation & Independence**: Each test scenario is designed to be atomic, handling its own setup (e.g., login, data creation) and teardown. This is crucial for reliable parallel execution
- **Page Object Model (POM) for API**: While not a traditional UI POM, POJO (Plain Old Java Object) classes are used to model API request and response bodies, enhancing readability and maintainability
- **Utility Classes**: Common functionalities like `RequestSpecification` builders and `ConfigReader` are centralized in dedicated utility classes for reusability and cleaner step definitions
- **Externalized Configuration**: Sensitive and environment-specific data is kept in `config.properties`, which is external to the codebase and ignored by Git
- **Comprehensive Logging**: Request and response details are logged to `logging.txt` for easy debugging (and this file is also ignored by Git)

## 🤝 Contributing

Contributions are welcome! If you have suggestions for improvements, new features, or bug fixes, please feel free to:

1. Fork the repository
2. Create a new branch (`git checkout -b feature/your-feature-name`)
3. Make your changes
4. Commit your changes (`git commit -m 'Add new feature'`)
5. Push to the branch (`git push origin feature/your-feature-name`)
6. Open a Pull Request

## 📧 Contact

For any questions or feedback, please reach out to:

**DeepikaJpr** - [GitHub Profile](https://github.com/DeepikaJpr)
