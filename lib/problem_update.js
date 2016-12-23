
/**
*
*@api {put} /problem/:_id 更新问题
*@apiVersion 0.1.0
*@apiName ProblemUpdate
*@apiGroup Problem
*@apiPermission User
*
*
*@apiParam {String} title 问题题目
*@apiParam {String} description 问题描述
*
*
*@apiSuccess {Number} code 0
*
*
*/


const co = require('co');

const Problem = require('../models/problem');

const OnError = require('./on_error');

function ProblemUpdate(req, res, next) {
  co(function * () {
    let problem =
        yield Problem.findByIdAndUpdate(req.params._id, {$set: req.body})
            .exec();
    res.json({code: 0, problem});
  }).catch(OnError(req, res));
}

module.exports = ProblemUpdate;