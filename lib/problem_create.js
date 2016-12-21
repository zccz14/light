const co = require('co');

const Problem = require('../models/problem');
const User = require('../models/user');

const OnError = require('./on_error');

function ProblemCreate(req, res, next) {
    co(function* () {
        let user = req.session.user;
        let problem = new Problem({
            ownerId: user._id,
            title: (req.body.title || '').trim(),
            description: (req.body.description || '').trim()
        });
        problem = yield problem.save();
        user = yield User.findByIdAndUpdate(user._id, {
            $addToSet: {
                problems: problem._id
            }
        }, { new: true }).exec();
        req.session.user = user;
        res.json({ code: 0, user });
    }).catch(OnError(req, res));
}

module.exports = ProblemCreate;