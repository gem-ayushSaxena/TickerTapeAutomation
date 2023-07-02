# TickerTape-Automation
This is Ticker Tape website automation using Selenium. It uses Selenium in Java, Cucumber to create a feature file and apache.POI dependency to dump data in excel.

# Project SetUp
1. Clone this repository.
2. Install the following dependencies:
   * Install Apache Maven: [Maven Installation Guide](https://maven.apache.org/install.html) since this is a Maven Project.
   * You will need Java SDK on your local.

# Run the Project
1. Post performing the above mentioned steps open the project in any editor of your choice(I have used IntelliJ for building this project).
2. Navigate to the Runner file under `src/main/java/RunnerFiles/Runner`.
3. Hit the Run Button.

# Project Structure
1. `src/main/java/RunnerFiles` It contains the Runner file which glue the Stepdefinition and the feature file together.
2. `src/main/java/StepDefinitions/StepDefinition` It contains the definition of each of the feature defined in Feature file.
3. `src/main/java/StepDefinitions/driverClass` It contains the initialization of Webdriver.
4. `Features` It contains the Gherkin feature file which defines the scenarios covered here.
5. `pom.xml` It contains all the dependencies required for this project.

# Dependencies Used
* Cucumber
* Selenium
* Apache Maven
  
# Results
![image](https://github.com/GemAyush/TickerTapeAutomation/assets/125482096/a84be54c-368b-4f94-9ccd-8c7512772311)
![image](https://github.com/GemAyush/TickerTapeAutomation/assets/125482096/c90dd77a-27f2-4b22-a73a-a70b3fb956f2)

