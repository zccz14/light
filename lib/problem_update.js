const co = require('co');

const Problem = require('../models/problem');

const OnError = require('./on_error');

function ProblemUpdate(req, res, next) {
  co(function * () {
    let{user} = req.session;
    let{problemId} = req.params;
    let{title, description} = req.body;
    let problem = yield Problem.findById(problemId).exec();
    if (problem === null || problem.ownerId.toString() !== user._id) {
      res.json({code: 11, msg: 'Problem Not Available', problemId});
      return;
    }
    Object.assign(problem, {title, description});
    problem = yield problem.save();
    res.json({code: 0, problem});
  }).catch(OnError(req, res));
}

module.exports = ProblemUpdate;