const co = require('co');
const mongoose = require('mongoose');
const ObjectId = mongoose.Types.ObjectId;

const Problem = require('../models/problem');
const ProblemList = require('../models/problem_list');

const OnError = require('./on_error');

function ProblemListDetail(req, res, next) {
    co(function* () {
        let problemList = yield ProblemList.findById(req.params._id).exec();
        if (problemList === null) {
            res.json({ code: 11 });
            return;
        }
        problemList.problems = yield Problem.find({
            _id: {
                $in: problemList.problems.map(v => new ObjectId(v))
            }
        }).exec();
        res.json({
            code: 0,
            problemList
        });
    }).catch(OnError(req, res));
}

module.exports = ProblemListDetail;