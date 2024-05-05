# Software Quality Engineering - System Testing
This is a repository for the system-testing assignment of the Software Quality Engineering course at the [Ben-Gurion University](https://in.bgu.ac.il/), Israel.

## Assignment Description
In this assignment, we tested an open-source software called [Moodle](https://Moodle.com).

Moodle serves as an open-source learning management system, facilitating the creation and administration of online courses. Within its framework, users access an array of tools and functionalities for content creation, delivery, assignment and quiz management, as well as student progress tracking.

## Installation
- [Moodle windows installer](https://download.moodle.org/windows/). We Used Moodle 4.3.3+ which is said to be 4.0.3
- Java with Maven tool to build Cucumber project.
- Selenium Chrome webdriver. Webdriver path has to be added to $PATH to run Selenium standalone server for Provengo part.
- Selenium server .jar for Provengo.
- Browser with "xPath Finder" extension in order to find xpaths.


## What we tested
We tested the forum module that allows for creating and deleted discussions. We chose to test the following user stories: *We assume that there is "Grades" discussion in ths Forum*.

**User story:** A student subscribes to a "Grades" discussion in "ForumQA".

**Preconditions:** There is a discussion calls "Grades" and Course names "Software Quality Engineering". 

**Expected outcome:** Subscribe confirmation message to the student.

**User story:** A teacher deletes "Grades" discussion.

**Preconditions:** There is a discussion called "Grades" and Course names "Software Quality Engineering".

**Expected outcome:** Delete confirmation message to the teacher. *When the teacher delete the Grades discussion before the student click on subscribe - the student will get an error message when he clicks*



## How we tested
We used two different testing methods:
1. [Cucumber](https://cucumber.io/), a behavior-driven testing framework.
2. [Provengo](https://provengo.tech/), a story-based testing framework.

Each of the testing methods is elaborated in its own directory. 

## Results
1. [Cucumber README.md](https://github.com/BGU-SE-Courses/2023-mbt-54-40/blob/main/Cucumber/README.md)
2. [Provengo README.md](https://github.com/BGU-SE-Courses/2023-mbt-62/blob/main/Provengo/README.md)
