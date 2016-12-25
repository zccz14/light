/**
 * @module
 * @author zccz14
 */

const co = require('co');

const {Problem} = require('../models');

const OnError = require('./on_error');

/**
 * @name ProblemDetail
 * @description 查看题目详情的中间件
 * @function
 * @param {Object} req - HTTPRequest
 * @param {Object} res - HTTPResponse
 * @param {Object} next - 下一个中间件
 * @returns {void}
 * 
 * @api {get} /problem/:problemId 查询题目详情
 * @apiDescription 用户查看自己创建的题目
 * @apiVersion 0.1.0
 * @apiName ProblemDetail
 * @apiGroup Problem
 * @apiPermission user
 *
 * @apiParam {ObjectId} problemId 题目ID
 *
 * @apiSuccess {Number} code 0
 * @apiSuccess {Problem} problem 完整的题目文档
 * 
 */

function ProblemDetail(req, res, next) {
    co(function*(){
        let {user} = req.session;
        let {problemId} = req.params;
        let problem = yield Problem.findById(problemId).exec();
        if (problem === null || problem.ownerId.toString() !== user._id) {
            res.json({code: 11, msg: 'Problem Not Available', problemId});
            return;
        }
        res.json({code: 0, problem});
    }).catch(OnError(req, res));
}

module.exports = ProblemDetail;