const co = require('co');

const Problem = require('../models/problem');

const OnError = require('./on_error');

function ProblemCreate(req, res, next) {
    co(function* () {
        let newProblem = new Problem({
            title: (req.body.title || '').trim(),
            description: (req.body.description || '').trim(),
        });
        newProblem = yield newProblem.save();
        res.json({ code: 0 });
    }).catch(OnError(res));
}

module.exports = ProblemCreate;