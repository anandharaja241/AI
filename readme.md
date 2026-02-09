#### Prerequisites
- Java 11 or higher
- Maven 3.6+
- JavaFX SDK

#### Project Setup
1. Clone the repository
2. Navigate to the project directory
3. Copy `.env.sample` to `.env` and add your API keys/credentials
4. Run `mvn clean install` to download dependencies

#### How It Works
This resume analyzer application helps you parse and analyze resume documents. It extracts key information such as skills, experience, education, and contact details using AI/NLP processing.

#### Running the Application
Execute the Maven command to start the JavaFX GUI:
```
mvn javafx:run
```

#### Features
- Upload and parse resume files (PDF)
- Extract and display resume components
- Analyze skills and experience
- Generate insights and recommendation score
