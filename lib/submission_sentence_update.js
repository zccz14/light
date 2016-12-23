/**
 * Submission Sentence Update
 * @module
 * @author zccz14 <zccz14@outlook.com>
 */

const co = require('co');

const {User, Submission} = require('../models');

const OnError = require('./on_error');

/**
 * @api {put} /submission/:submissionId/sentence 更新判决
 * @apiVersion 0.1.0
 * @apiName SubmissionSentenceUpdate
 * @apiGroup Submission
 * @apiPermission Judger
 *
 * @apiParam {ObjectId} submissionId 提交ID
 * @apiParam {String} sentence 新判决
 *
 * @apiSuccess {Number} code 0
 * @apiSuccess {Object} submission 新的提交文档
 * @apiDescription
 * 被指定作为 Judger(裁判) 用户才可使用此API。
 */

function SubmissionSentenceUpdate(req, res, next) {
  co(function * () {
    let submissionId = req.params.submissionId;
    let{sentence} = req.body;
    let{user} = req.session;
    let submission = yield Submission.findById(submissionId).exec();
    if (submission.judgerId.toString() === user._id) {
      submission.sentence = sentence;
      submission = yield submission.save();
      res.json({code: 0, submission});
    } else {
      res.json({
        code: 7,
      });
    }
  }).catch(OnError(req, res));
}

module.exports = SubmissionSentenceUpdate;