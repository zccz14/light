const co = require('co');

const Problem = require('../models/problem');

const OnError = require('./on_error');

function ProblemUpdate(req, res, next) {
    co(function* () {
        let problem = yield Problem.findByIdAndUpdate(req.params._id, {
            $set: req.body
        }).exec();
        res.json({ code: 0, problem });
    }).catch(OnError(res));
}

module.exports = ProblemUpdate;