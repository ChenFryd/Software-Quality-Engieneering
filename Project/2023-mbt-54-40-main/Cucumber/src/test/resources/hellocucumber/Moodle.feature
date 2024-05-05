Feature: A student subscribes to a "Grades" discussion and the teacher deletes the discussion

  Scenario: Student logs in and do subscribe to Grades discussion in the forum
    Given Student is on Home Page
    And Student Navigate to LogIn Page
    When Student enters UserName "student" and Password "Student#1"
    And Hi, "student"! 👋 message displays
    Then The Student should be taken to the Moodle dashboard
    And The Student clicks on My courses
    Then Student should see all his courses
    And Student clicks on the link to the "Software Quality Engineering" course page
    Then the Student should see a link to the forum page
    When Student clicks on the link to the "Forum" link
    Then all the discussions should be displayed on the Course Forum page
    When Student clicks on subscribe button on "Grades" discussion
    Then the Student should see a confirmation massage


  Scenario: Teacher logs in and delete Grades discussion from the forum
    Given Teacher is on Home Page
    And Teacher Navigate to LogIn Page
    When Teacher enters UserName "teacher" and Password "Teacher#1"
    And Hi, "teacher"!  👋 message displays
    Then The Teacher should be taken to the Moodle dashboard
    And The Teacher clicks on My courses
    Then Teacher should see all his courses
    And Teacher clicks on the link to the "Software Quality Engineering" course page
    Then The Teacher should see a link to the forum page
    When Teacher clicks on the link to the "Forum" link
    And the Teacher clicks on the "Grades" discussion
    And clicks on the delete button
    Then the Teacher should be prompted to confirm the deletion
    When the Teacher clicks Continue to confirm the deletion
    Then the discussion should be removed from the forum page and the Teacher should see a message indicating that the discussion was deleted successfully



  Scenario: A student subscribes to a forum discussion while the teacher deletes the discussion
    Given Student and Teacher is on Home Page
    And Student Navigate to LogIn Page
    When Student enters UserName "student" and Password "Student#1"
    And Hi, "student"! 👋 message displays
    Then The Student should be taken to the Moodle dashboard
    And The Student clicks on My courses
    Then Student should see all his courses
    And Student clicks on the link to the "Software Quality Engineering" course page
    Then the Student should see a link to the forum page
    When Student clicks on the link to the "Forum" link
    Then all the discussions should be displayed on the Course Forum page
    And Teacher Navigate to LogIn Page
    When Teacher enters UserName "teacher" and Password "Teacher#1"
    And Hi, "teacher"!  👋 message displays
    Then The Teacher should be taken to the Moodle dashboard
    And The Teacher clicks on My courses
    Then Teacher should see all his courses
    And Teacher clicks on the link to the "Software Quality Engineering" course page
    Then The Teacher should see a link to the forum page
    When Teacher clicks on the link to the "Forum" link
    And the Teacher clicks on the "Grades" discussion
    And clicks on the delete button
    Then the Teacher should be prompted to confirm the deletion
    When the Teacher clicks Continue to confirm the deletion
    Then the discussion should be removed from the forum page and the Teacher should see a message indicating that the discussion was deleted successfully
    When Student clicks on subscribe button on "Grades" discussion
    Then the Student should get error massage