/**
 * Problem Create
 * @module Problem Create
 * @author zccz14 <zccz14@outlook.com>
 * @requires co
 * @requires ../models
 * @requires ./on_error
 */
const co = require('co');

const Problem = require('../models/problem');

const OnError = require('./on_error');
/**
 * @typedef {Object} ProblemResBody
 * @property {number} code 错误码
 * @property {Problem} problem 问题
 */
/**
 * @name ProblemCreate
 * @author zccz14
 * @function
 * @param {String} title - 题目标题
 * @param {String} description - 题目描述
 * @returns {ProblemResBody} problem - 新建的问题
 * @desc 每个用户都可以向 Problem Collection 中添加问题。
 * 
 * @api {post} /problem 创建题目
 * @apiVersion 0.1.0
 * @apiName ProblemCreate
 * @apiGroup Problem
 * @apiPermission user
 * 
 * @apiParam {String} title 题目标题
 * @apiParam {String} description 题目描述
 * 
 * @apiSuccess {Number} code 0
 * @apiSuccess {Object} problem 题目文档
 */
function ProblemCreate(req, res, next) {
  co(function * () {
    let user = req.session.user;
    let problem = new Problem({
      ownerId: user._id,
      title: (req.body.title || '').trim(),
      description: (req.body.description || '').trim()
    });
    problem = yield problem.save();
    res.json({code: 0, problem});
  }).catch(OnError(req, res));
}

module.exports = ProblemCreate;