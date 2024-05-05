// @provengo summon ctrl

/**
 * List of events "of interest" that we want test suites to cover.
 */
const GOALS = {
    ctrlInit: Event('initiatedTest'),
    ctrlLogInStudent: Event('LogInStudent'),
    ctrlLogInTeacher: Event('LogInTeacher'),
    ctrlDeleteDiscussionTeacher: Event('deleteDiscussionTeacher'),
    ctrlSubscribeDiscussionStudent: Event('subscribeGradesDiscussionStudent'),

    //loginDouble
    ctrlLogInStudentLogInTeacherStudentSubscribeDeleteDiscussion: Ctrl.markEvent('CtrlLogInStudent_LogInTeacher_SubscribeGradesDiscussionStudent_DeleteGradesDiscussionTeacher'),
    ctrlLogInTeacherLogInStudentStudentSubscribeDeleteDiscussion: Ctrl.markEvent('CtrlLogInStudent_LogInTeacher_SubscribeGradesDiscussionStudent_DeleteGradesDiscussionTeacher'),
    //singleRepeat
    ctrlLogInStudentSubscribeLogInTeacherDeleteDiscussion: Ctrl.markEvent('CtrlLogInStudent_SubscribeGradesDiscussionStudent_LogInTeacher_DeleteGradesDiscussionTeacher'),
    ctrlLogInStudentLogInTeacherDeleteDiscussion: Ctrl.markEvent('CtrlLogInStudent_LogInTeacher_DeleteGradesDiscussionTeacher'),
    ctrlLogInTeacherLogInStudentDeleteDiscussion: Ctrl.markEvent('CtrlLogInTeacher_LogInStudent_DeleteGradesDiscussionTeacher'),
    ctrlLogInTeacherDeleteDiscussion: Ctrl.markEvent('CtrlLogInTeacher_DeleteGradesDiscussionTeacher'),

};

const sequenceGoal = function(){
    return [
             [ GOALS.ctrlInit, GOALS.ctrlLogInStudent ],
             [ GOALS.ctrlInit, GOALS.ctrlLogInTeacher ],
             [ GOALS.ctrlLogInTeacher, GOALS.ctrlDeleteDiscussionTeacher ],
             [ GOALS.ctrlLogInStudent, GOALS.ctrlSubscribeDiscussionStudent]
    ];
}

//all of the two feasible ways
const twoWayGoal = function(){
    return [
        GOALS.ctrlLogInStudentLogInTeacherStudentSubscribeDeleteDiscussion,
        GOALS.ctrlLogInTeacherLogInStudentStudentSubscribeDeleteDiscussion,
        GOALS.ctrlLogInStudentSubscribeLogInTeacherDeleteDiscussion,
        GOALS.ctrlLogInStudentLogInTeacherDeleteDiscussion,
        GOALS.ctrlLogInTeacherLogInStudentDeleteDiscussion,
        GOALS.ctrlLogInTeacherDeleteDiscussion
    ];
}

/**
 * Two-Way criterion ranking function. Calculates and sums the score of each test in the ensemble.
 * The score is based on the coverage of the combinations.
 *
 * @param {Event[][]} ensemble test suite/ensemble to be ranked
 * @returns the percentage of the covered scenarios' combinations
 */
function twoWaySpecificRankingFunction( ensemble ) {
    let score = 0
    //let goalStatus = new Array(goals.length).fill(false);
    let goals = twoWayGoal().map(e => e.name)
    for (let test of ensemble) {
        for (let evt of test) {
            if (goals.includes(evt.name)) {
                score += 1
                break
            }
        }
    }
    return 100 * score / ensemble.length
}

/**
 * Domain-specific criterion ranking function. Calculates and sums the score of each test in the ensemble.
 * The score is based on the coverage of the sequences.
 *
 * @param {Event[][]} ensemble test suite/ensemble to be ranked
 * @returns the percentage of the covered scenarios' sequences
 */
function domainSpecificRankingFunction( ensemble ) {
    let score = 0.0
    for ( let test of ensemble ) {
        score += rankGoal(test)
    }
    return score / (ensemble.length * sequenceGoal().length) * 100
}

function rankGoal(test) {
    let score = 0.0
    let goals = sequenceGoal()
    for (let goal of goals) {
        let i = 0
        for (let event of test) {
            if (goal[i].name === event.name)
                i++
            if (i === goal.length) {
                score += 1
                break
            }
        }
    }
    return score
}


/**
 * Ranks potential test suites based on the percentage of goals they cover.
 * Goal events are defined in the GOALS array above. An ensemble with rank
 * 100 covers all the goal events.
 *
 * Multiple ranking functions are supported - to change ranking function,
 * use the `ensemble.ranking-function` configuration key, or the 
 * --ranking-function <functionName> command-line parameter.
 *
 * @param {Event[][]} ensemble the test suite/ensemble to be ranked
 * @returns the percentage of goals covered by `ensemble`.
 */
 function rankingFunction(ensemble) {
    //return domainSpecificRankingFunction(ensemble)
    return twoWaySpecificRankingFunction(ensemble)
}
