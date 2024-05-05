/* @provengo summon selenium */
/* @provengo summon ctrl */

//control point for two-way
let ctrls = {
    ctrlInit: Ctrl.markEvent("CtrlInitiatedTest"),
    ctrlLogInStudent: Ctrl.markEvent("CtrlLogInStudent"),
    ctrlLogInTeacher: Ctrl.markEvent("CtrlLogInTeacher"),
    ctrlDeleteGradesDiscussionTeacher: Ctrl.markEvent("CtrlDeleteGradesDiscussionTeacher"),
    ctrlSubscribeGradesDiscussionStudent: Ctrl.markEvent("CtrlSubscribeGradesDiscussionStudent"),
}

//initate test. creating a discussion
bthread('Teacher creates Grades discussion', function () {
    initiateTest()
    sync({request : Event("initiatedTest")})
    sync({request: ctrls.ctrlInit})
})

// Teacher story: The teacher delete Grades discussion from the forum.
// We want to block any try of student to subscribe this discussion after the delete
bthread('Teacher delete Grades discussion', function () {
    sync({waitFor : Event("initiatedTest")})
    let teacherSession = new SeleniumSession("TeacherDeletes").start(URLgradesDiscussion)
    sync({request : Event("LogInTeacher",login(teacherSession, {username: 'teacher', password: 'Teacher#1'}))})
    sync({request: ctrls.ctrlLogInTeacher})
    sync({request : Event("goToGradesDiscussionTeacher",goToGradesDiscussion(teacherSession))})
    sync({request : Event("deleteDiscussionTeacher",deleteDiscussion(teacherSession))})
    sync({request: ctrls.ctrlDeleteGradesDiscussionTeacher})
    teacherSession.close()
})

// Student story: The student subscribe the Grades discussion in the forum
bthread('Student subscribes Grades discussion', function () {
    sync({waitFor : Event("initiatedTest")})
    let studentSession = new SeleniumSession("StudentSubscribes").start(URLgradesDiscussion)
    sync({request : Event("LogInStudent",login(studentSession,{username: 'student', password: 'Student#1'}))})
    sync({request: ctrls.ctrlLogInStudent})
    sync({request : Event("subscribeGradesDiscussionStudent",subscribeGradesDiscussion(studentSession))})
    sync({request: ctrls.ctrlSubscribeGradesDiscussionStudent})
    studentSession.close()
})

// can't subscribe anymore after the teacher deletes!
bthread('Cant subscribe after teacher deletes', function () {
    sync({waitFor : Event("deleteDiscussionTeacher")})
    sync({block : Event("subscribeGradesDiscussionStudent")})
})


// marks the sequence of double events.
// student, student or teacher, teacher
let doubleRepeatedActions = [
    ["Student","Student","Teacher","Teacher"],
    ["Teacher","Teacher","Student","Student"]
]

//input: role( student, teacher)
//output: action (subscribe, delete)
const getAction = (role) => {
    switch (role) {
        case "Student":
            return 'SubscribeGradesDiscussion';
        case "Teacher":
            return 'DeleteGradesDiscussion';
        default:
            return ''; // not supposed to get in here
    }
};

// a,b,c,d is the roles who take the action
// this is the function for two-way if a,b is the same actor. for example: a=Teacher, b=Teacher, or a=student,b=student,c=teacher,d=teacher
doubleRepeatedActions.forEach(([a,b,c,d]) => {
    bthread(`StudentTeacherDoubleRepeated (${a}, ${b}, ${c}, ${d})`, function () {
        sync({waitFor: Ctrl.markEvent(`CtrlLogIn${a}`)});
        sync({ waitFor: Ctrl.markEvent(`Ctrl${getAction(b)}${b}`) });
        if (b === "Teacher") { //if the first action is delete, do not wait for student
            sync({request: Ctrl.markEvent(`CtrlLogIn${a}_${getAction(b)}${b}`)});
        }
        else {
            sync({waitFor: Ctrl.markEvent(`CtrlLogIn${c}`)});
            sync({waitFor: Ctrl.markEvent(`Ctrl${getAction(d)}${d}`)});
            sync({request: Ctrl.markEvent(`CtrlLogIn${a}_${getAction(b)}${b}_LogIn${c}${c}_${getAction(d)}${d}`)})

        }
    })
})

//marks the sequence of single repeated events.
// student, teacher or teacher, student
let singleRepeatedAction = [
    ["Student","Teacher","Student","Teacher"],
    ["Student","Teacher","Teacher","Student"],
    ["Teacher","Student","Teacher","Student"],
    ["Teacher","Student","Student","Teacher"]
]
singleRepeatedAction.forEach(([a,b,c,d]) => {
    bthread(`StudentTeacherSingleRepeated (${a}, ${b}, ${c}, ${d})`, function () {
        sync({waitFor: Ctrl.markEvent(`CtrlLogIn${a}`)});
        sync({waitFor: Ctrl.markEvent(`CtrlLogIn${b}`)});
        sync({waitFor: Ctrl.markEvent(`Ctrl${getAction(c)}${c}`)});
        if (c === "Teacher") { //if the first action is delete, do not wait for student (if the action is deleting, the student can't subscribe)
            sync({ request: Ctrl.markEvent(`CtrlLogIn${a}_LogIn${b}_${getAction(c)}${c}`)})
        }
        else{ //the student manager to subscribe, then wait for the teacher to delete.
            sync({ waitFor: Ctrl.markEvent(`Ctrl${getAction(d)}${d})`)}); //waiting for teacher deletion
            sync({ request: Ctrl.markEvent(`CtrlLogIn${a}_LogIn${b}_${getAction(c)}${c}_${getAction(d)}${d}`)})
        }
    })
})