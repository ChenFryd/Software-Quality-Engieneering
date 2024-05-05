# Testing Moodle Forum using Cucumber
This directory contains the cucumber files for testing the forum module of the moodle application.

## Running the tests
Run ```mvn test``` to run all the tests.

## Feature files
The behaviors that we tested are in the feature files that inside the [resources/hellocucumber](resources/hellocucumber) directory. See the files for a detailed description of the tests.

## Step files
The step files in the [src/test/java/hellocucumber](src/test/java/hellocucumber) directory contain the code that defines how each sentence in the feature files is translated to Selenium actions. See the files for a detailed description of the implementation.


## Prerequesites
- Course
- Moodle teacher
- One or more students
- Forum module
- Discussion
- Grades discussion

## User Stories
- A student subscribes to a "Grades" discussion in "ForumQA".
- A teacher deletes "Grades" discussion.