/**
 * Problem Create
 * Author: zccz14
 * Authority: 需要登录
 * Request:
 *   body:
 *     title: 题目标题
 *     description: 题目描述
 * Response:
 *   body:
 *     problem: 新插入的问题的文档
 * Comment:
 *   每个用户都可以向 Problem Collection 中添加问题。
 */
const co = require('co');

const Problem = require('../models/problem');

const OnError = require('./on_error');

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