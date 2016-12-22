const co = require('co');

const Problem = require('../models/problem');

const OnError = require('./on_error');

function ProblemDelete(req, res, next) {
  co(function * () {
    let problem = yield Problem.findByIdAndRemove(req.params._id).exec();
    res.json({code: 0, problem});
  }).catch(OnError(req, res));
}

module.exports = ProblemDelete;