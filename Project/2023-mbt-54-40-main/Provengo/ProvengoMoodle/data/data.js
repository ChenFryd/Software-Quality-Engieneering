/*
 *  This is a good place to put common test data, project-wide constants, etc.
 */

const URLgradesDiscussion = 'http://localhost/mod/forum/view.php?id=2';

const xpaths = {
  homePage: {
    logInButton: '//span[@class="login pl-2"]'
  },
  logInPage: {
    userBox: '/html/body/div[2]/div[2]/div/div/section/div/div/div/div/form[1]/div[1]/input',
    passwordBox: '/html/body/div[2]/div[2]/div/div/section/div/div/div/div/form[1]/div[2]/input',
    logInButton: '/html/body/div[2]/div[2]/div/div/section/div/div/div/div/form[1]/div[3]/button'
  },
  mainLogInPage: {
    userBox: '//input[@name="username"]',
    passwordBox: '//input[@name="password"]',
    logInButton: '//button[@class="btn btn-primary btn-lg"]'
  },
  userHomePage: {
    coursesPageButton: '//a[@class="nav-link  "]' //"//a[@href='http://localhost/mod/forum/view.php?id=2']"
  },
  coursesPage: {
    course: '//a[@href="http://localhost/course/view.php?id=2"]' ///a[@class="aalink coursename mr-2 mb-1"]' /
  },
  coursePage: {
    forum: '//*[@class=" aalink stretched-link"]' //href="http://localhost/mod/forum/view.php?id=2
  },
  forumPage: {
    gradesDiscussion: "//*[@class='w-100 h-100 d-block' and contains(text(),'Grades')]", //a[@href="http://localhost/mod/forum/discuss.php?d=3"]
    subscribe: "//div[@class='d-inline custom-control custom-switch mb-1']",
  },
  gradesPage: {
    create: "//a[@class='btn btn-primary' and contains(text(),'Add discussion topic')]",
    delete: '/html/body/div[2]/div[4]/div/div[3]/div/section/div[2]/div/article/div[1]/div/div/div[2]/div[2]/div/a[3]',
    continue: "//*[@class='btn btn-primary' and contains(text(),'Continue')]",
    subject: "//input[@id='id_subject']",
    message: "//html/body/div[3]/div[4]/div/div[3]/div/section/div[2]/div[2]/div[2]/form/div[3]/div[2]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/iframe", //*[@id=\"id_message_ifr\"]",
    post: "//input[@id='id_submitbutton']",
  }

}