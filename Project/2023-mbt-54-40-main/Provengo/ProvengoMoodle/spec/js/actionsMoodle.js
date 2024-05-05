/* @Provengo summon selenium */

function login(session, data) {
    with(session){
        session.writeText(xpaths.logInPage.userBox, data.username);
        session.writeText(xpaths.logInPage.passwordBox, data.password);
        session.click(xpaths.logInPage.logInButton);
    }
}
function mainLogin(session, data) {
    with(session){
        session.click(xpaths.homePage.logInButton);
        session.writeText(xpaths.logInPage.userBox, data.username);
        session.writeText(xpaths.logInPage.passwordBox, data.password);
        session.click(xpaths.logInPage.logInButton);
    }
}
function goToCoursesPage(session) {
    with(session){
        session.click(xpaths.userHomePage.coursesPageButton);
    }
}

function goToCourse(session) {
    with(session){
        session.click(xpaths.coursesPage.course);
    }
}

function goToForum(session) {
    with(session) {
        session.click(xpaths.coursePage.forum);
    }
}
function goToGradesDiscussion(session) {
    with(session){
        session.click(xpaths.forumPage.gradesDiscussion);
    }
}

function subscribeGradesDiscussion(session) {
    if (session == null)
        return;
    with(session){
        session.click(xpaths.forumPage.subscribe);
    }
}

function deleteDiscussion(session) {
    if (session == null)
        return;
    with(session){
        //session.waitForVisibility(xpaths.gradesPage.delete);
        session.click(xpaths.gradesPage.delete);
        //session.waitForVisibility(xpaths.gradesPage.continue);
        session.click(xpaths.gradesPage.continue);
    }
}

function createDiscussion(session) {
    if (session == null)
        return;
    with(session){
        session.click(xpaths.gradesPage.create);
        session.waitForVisibility(xpaths.gradesPage.subject);
        session.writeText(xpaths.gradesPage.subject, "Grades",true);
        session.waitForVisibility(xpaths.gradesPage.subject);
        session.writeText(xpaths.gradesPage.message, "GradesMessage",true);
        session.waitForVisibility(xpaths.gradesPage.subject);
        session.click(xpaths.gradesPage.post);
    }
}

function initiateTest(){
    let sessionInitiateTest = new SeleniumSession("initiateTest").start(URLgradesDiscussion)
    login(sessionInitiateTest, {username: 'teacher', password: 'Teacher#1'});
    // goToCoursesPage(sessionInitiateTest);
    // goToCourse(sessionInitiateTest);
    // goToForum(sessionInitiateTest);
    createDiscussion(sessionInitiateTest);
    sessionInitiateTest.close();

}
function assertDiscussionAvailability(session,data){
    //session.assert(session.availability, event.availability);
    // session.click("//*[@class='btn btn-primary' and contains(text(),'Continue')]");
    // session.assertText('xpath', event.availability);
    if (session.availability != data.availability)
        throw new Error("session availability: "+session.availability+" is not equal to event availability "+event.availability);
}

function changeAvailableDiscussion(session,data){
    // Get the availability value from the session variable
    session.availability = data.availability
}
