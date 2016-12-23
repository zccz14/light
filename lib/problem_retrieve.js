/**
*
*@api {get} /problem 问题检索
*@apiVersion 0.1.0
*@apiName ProblemRetrieve
*@apiGroup Problem
*@apiPermission User
*
*
*@apiParam {String} key 关键字
*@apiParam {Number} limit 查询的题目上限
*@apiParam {Number} skip 跳过的题目数
*
*
*@apiSuccess {Number} code 0
*
*
*
*/

const co = require('co');

const ObjectId = require('mongoose').Types.ObjectId;
const Problem = require('../models/problem');

const OnError = require('./on_error');

function ProblemRetrieve(req, res, next) {
  co(function * () {
    let user = req.session.user;

    let{limit = 15, skip = 0, key = ''} = req.query;
    let key = RegExp(key);

    let problems = yield Problem.find({
                                  ownerId: ObjectId(user._id),
                                  $or: [{title: key}, {description: key}]
                                })
                       .limit(parseInt(limit))
                       .skip(parseInt(skip))
                       .exec();
    res.json({code: 0, problems});
  }).catch(OnError(req, res));
}

module.exports = ProblemRetrieve;