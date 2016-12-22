/**
 * Problem List Problem Create
 * 将题目添加到题单中
 * @author {"zccz14"}
 * Comment:
 *   用户
 */

const co = require('co');

const {User, Problem, ProblemList} = require('../models');
const ProblemVisiblity = require('./problem_visibility');

function ProblemListProblemCreate(req, res, next) {
  co(function * () {
    let{problemId, problemListId} = req.body;
    let user = req.session.user;
    let userId = user._id;
    // let problem = yield Problem.findById(problemId).exec();
    // TODO: Check Visibility of problem
    // TODO: Check Authority of problem
    // TODO: Check Visibility of problemList
    // TODO: Check Authority of problemList
    // Is the problem visible to the user?
    // if (problem)
    // if (problemList === null || problemList.ownerId !== userId)
    let problemList =
        yield ProblemList.findByIdAndUpdate(problemListId,
                                            {$addToSet: {problems: problemId}},
                                            {new: true})
            .exec();
    res.json({code: 0, problemList});
  });
}

module.exports = ProblemListProblemCreate;