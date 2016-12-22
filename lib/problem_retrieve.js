const co = require('co');

const Problem = require('../models/problem');

const OnError = require('./on_error');

function ProblemRetrieve(req, res, next) {
  co(function * () {
    let problems = yield Problem.find(req.query).exec();
    res.json({code: 0, problems});
  }).catch(OnError(req, res));
}

module.exports = ProblemRetrieve;