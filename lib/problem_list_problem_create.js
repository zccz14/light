/**
 * Problem List Problem Create
 * @module
 * @author zccz14 <zccz14@outlook.com>
 */

const co = require('co');

const {User, Problem, ProblemList} = require('../models');
const ProblemVisiblity = require('./problem_visibility');

/**
 * @api {post} /problem_list/:problemListId/problem 添加题目
 * @apiVersion 0.1.0
 * @apiName ProblemListProblemCreate
 * @apiGroup ProblemList
 * @apiPermission user
 *
 * @apiParam {ObjcetId} problemListId 题单ID
 * @apiParam {ObjectId} problemId 题目ID
 *
 * @apiSuccess {Number} code 0
 * @apiSuccess {Object} problemList 题单文档
 *
 */
function ProblemListProblemCreate(req, res, next) {
  co(function * () {
    let problemId = req.body.problemId;
    let problemListId = req.params.problemListId;
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