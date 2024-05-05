package hellocucumber;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.ArrayList;
import java.util.List;

public class StepDefinitions {
    private String TEACHER_USERNAME = "teacher";
    private String TEACHER_PASSWORD = "Teacher#1";
    private static List<MoodleActuator> allMoodles;
    private static MoodleActuator moodleStud;
    private static MoodleActuator moodleTeach;
    private String webDriver = "webdriver.chrome.driver";
    private String path = "C:\\Users\\Chen\\Documents\\GitHub\\2023-mbt-54-40\\Selenium\\chromedriver.exe";
    public void moodleStudent() {
        System.out.println("--------------- INITIALIZING MOODLE TEST - OPENING WEBPAGE ---------------");
        if(allMoodles == null){
            allMoodles = new ArrayList<>();
        }
        moodleStud = new MoodleActuator();
        allMoodles.add(moodleStud);
        moodleStud.initSession(webDriver, path);
    }
    public void moodleTeacher() {
        System.out.println("--------------- INITIALIZING MOODLE TEST - OPENING WEBPAGE ---------------");
        if(allMoodles == null){
            allMoodles = new ArrayList<>();
        }
        moodleTeach = new MoodleActuator();
        allMoodles.add(moodleTeach);
        moodleTeach.initSession(webDriver, path);
    }

    //They both on home page, 2 web drivers are open.
    @Given("^Student and Teacher is on Home Page$")
    public void studentAndTeacherIsOnHomePage() {
        moodleStudent();
        moodleTeacher();
    }

    //Student go to logIn page.
    @And("^Student Navigate to LogIn Page$")
    public void studentNavigateToLogInPage() {
        moodleStud.goToLogin();
    }

    //Student insert his details
    @When("^Student enters UserName \"([^\"]*)\" and Password \"([^\"]*)\"$")
    public void studentEntersUserNameAndPassword(String username, String password) {
        moodleStud.enterLoginInfo(username, password);
    }

    //Massage displayed to student
    @And("^Hi, \"([^\"]*)\"! \uD83D\uDC4B message displays$")
    public void welcomeBackWelcomeWordMessageDisplays(String welcomeWord) {
        moodleStud.generalWelcomeMessage(welcomeWord);
    }


    //Student in the Dashboard page
    @Then("^The Student should be taken to the Moodle dashboard$")
    public void theStudentShouldBeTakenToTheMoodleDashboard() {
        String actualTitle = moodleStud.getTitle();
        assert actualTitle.equals("Dashboard") : "Actual title was: " + actualTitle;
    }

    //Student clicks on the course page
    @And("^The Student clicks on My courses$")
    public void theStudentClicksOnMyCourses() {
        moodleStud.goToCoursePage();
    }


    //Student see all of his courses
    @Then("^Student should see all his courses$")
    public void studentShouldSeeAllHisCourses() {
        String actualTitle = moodleStud.getTitle();
        assert actualTitle.equals("My courses") : "Actual title was: " + actualTitle;
    }

    //Student clicks on Software Quality Engineering course
    @And("^Student clicks on the link to the \"([^\"]*)\" course page$")
    public void studentClicksOnTheLinkToTheCoursePage(String courseName) {
        moodleStud.goToCourse(courseName);
    }


    //Student see all the forum page
    @Then("^the Student should see a link to the forum page$")
    public void theStudentShouldSeeALinkToTheForumPage() {
        String actualTitle = moodleStud.getTitle();
        assert actualTitle.equals("Course: Software Quality Engineering") : "Actual title was: " + actualTitle;
    }
    //Student clicks on Forum page
    @When("^Student clicks on the link to the \"([^\"]*)\" link$")
    public void studentClicksOnTheLinkToTheLink(String forumName) {
        moodleStud.goToForum();
    }

    //All the discussion should be displayed to the student
    @Then("^all the discussions should be displayed on the Course Forum page$")
    public void allTheDiscussionsShouldBeDisplayedOnTheCourseForumPage() {
        String actualTitle = moodleStud.getTitle();
        assert actualTitle.equals("Course Forum") : "Actual title was: " + actualTitle;
    }

    //Teacher go to logIn page.
    @And("^Teacher Navigate to LogIn Page$")
    public void teacherNavigateToLogInPage() {
        moodleTeach.goToLogin();
    }

    //Teacher insert his details
    @When("^Teacher enters UserName \"([^\"]*)\" and Password \"([^\"]*)\"$")
    public void teacherEntersUserNameAndPassword(String username, String password) {
        moodleTeach.enterLoginInfo(username, password);
    }

    //Message displayed to teacher
    @And("^Hi, \"([^\"]*)\"!  \uD83D\uDC4B message displays$")
    public void welcomeBackMessageDisplays(String welcomeWord) {
        moodleTeach.generalWelcomeMessage(welcomeWord);
    }

    //Teacher in the Dashboard page
    @Then("^The Teacher should be taken to the Moodle dashboard$")
    public void theTeacherShouldBeTakenToTheMoodleDashboard() {
        String actualTitle = moodleTeach.getTitle();
        assert actualTitle.equals("Dashboard") : "Actual title was: " + actualTitle;
    }
    //Teacher clicks on the course page
    @And("^The Teacher clicks on My courses$")
    public void theTeacherClicksOnMyCourses() {
        moodleTeach.goToCoursePage();
    }

    //Teacher see all of his courses
    @Then("^Teacher should see all his courses$")
    public void teacherShouldSeeAllHisCourses() {
        String actualTitle = moodleTeach.getTitle();
        assert actualTitle.equals("My courses") : "Actual title was: " + actualTitle;
    }

    //Teacher clicks on Software Quality Engineering course
    @And("^Teacher clicks on the link to the \"([^\"]*)\" course page$")
    public void teacherClicksOnTheLinkToTheCoursePage(String courseName) {
        moodleTeach.goToCourse(courseName);
    }

    //Teacher see all the forum page
    @Then("^The Teacher should see a link to the forum page$")
    public void theTeacherShouldSeeALinkToTheForumPage() {
        String actualTitle = moodleTeach.getTitle();
        assert actualTitle.equals("Course: Software Quality Engineering") : "Actual title was: " + actualTitle;
    }
    //Teacher clicks on Forum page
    @When("^Teacher clicks on the link to the \"([^\"]*)\" link$")
    public void teacherClicksOnTheLinkToTheLink(String forumName) {
        moodleTeach.goToForum();
    }
    //Teacher clicks on Grades discussion
    @And("^the Teacher clicks on the \"([^\"]*)\" discussion$")
    public void theTeacherClicksOnTheDiscussion(String discussionName) {
        moodleTeach.goToDiscussion(discussionName);
    }

    //Teacher clicks on delete button
    @And("^clicks on the delete button$")
    public void clicksOnTheButton() {
        moodleTeach.deleteDiscussion();
    }
    //Teacher should be asked if he sure
    @Then("^the Teacher should be prompted to confirm the deletion$")
    public void theTeacherShouldBePromptedToConfirmTheDeletion() {
        String actualTitle = moodleTeach.getTitle();
        assert actualTitle.equals("SQE") : "Actual title was: " + actualTitle;
    }
    //Teacher clicks on Continue button
    @When("^the Teacher clicks Continue to confirm the deletion$")
    public void theTeacherClicksToConfirmTheDeletion() {
        moodleTeach.pressContinue();
    }

    //The discussion should be deleted and massage should be displayed to the teacher
    @Then("^the discussion should be removed from the forum page and the Teacher should see a message indicating that the discussion was deleted successfully$")
    public void theDiscussionShouldBeRemovedFromTheForumPageAndTheTeacherShouldSeeAMessageIndicatingThatTheDiscussionWasDeletedSuccessfully() {
        String actualTitle = moodleTeach.getTitle();
        assert actualTitle.equals("Course Forum") : "Actual title was: " + actualTitle;
    }

    //Teacher clicks on subscribe to Grades forum
    @When("^Student clicks on subscribe button on \"([^\"]*)\" discussion$")
    public void clicksOnSubscribeButtonOnDiscussion(String discussionName) {
        moodleStud.clickSubscribe(discussionName);
    }
    //Student should get an error message
    @Then("the Student should get error massage")
    public void theStudentShouldBeSubscribedToTheDiscussion() {
        String actualTitle = moodleStud.getClassName();
        assert actualTitle.equals("toast-wrapper mx-auto py-0 fixed-top") : "Actual title was: " + actualTitle;
    }

    //Student is in home page - open web driver
    @Given("Student is on Home Page")
    public void studentIsOnHomePage() {
        moodleStudent();
    }
    //Teacher is in home page - open web driver
    @Given("Teacher is on Home Page")
    public void teacherIsOnHomePage() {
        moodleTeacher();
    }
    //Student should see a confirmation massage
    @Then("the Student should see a confirmation massage")
    public void theStudentShouldSeeAConfirmationMassage() {
        String actualTitle = moodleStud.getConfirmText();
        assert actualTitle.equals("notifications") : "Actual title was: " + actualTitle;
    }
}
