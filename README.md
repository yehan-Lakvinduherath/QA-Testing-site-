# DemoBlaze QA Automation Suite

An automated end-to-end web testing suite for the [DemoBlaze](https://www.demoblaze.com/) e-commerce application, built with Java, Selenium WebDriver, and TestNG.

## 🛠️ Tech Stack & Dependencies
* **Programming Language:** Java (JDK 25)
* **Browser Automation:** Selenium WebDriver (4.47.0)
* **Testing Framework:** TestNG (7.9.0)
* **Build Tool:** Apache Maven
* **Target Browser:** Google Chrome

## 🚀 Test Scenarios Covered
1. **`tc01_homePageSmokeTest`**: Home page load & brand title validation.
2. **`tc02_productSelectionTest`**: Category navigation & product detail extraction.
3. **`tc03_addToCartTest`**: Item cart addition & JavaScript alert interception.
4. **`tc04_cartManagementTest`**: Multi-item management, AJAX-synchronized deletion & price calculation.
5. **`tc05_checkoutValidationTest`**: Negative form validation & order placement confirmation.

## 💻 How to Run the Tests

### Prerequisites
* Java Development Kit (JDK 17 or higher)
* Apache Maven
* Google Chrome browser

### Execution via Command Line
Clone the repository and run the TestNG suite using Maven:

```bash
git clone [https://github.com/yehan-Lakvinduherath/QA-Testing-site-.git](https://github.com/yehan-Lakvinduherath/QA-Testing-site-.git)
cd QA-Testing-site-
mvn clean test
