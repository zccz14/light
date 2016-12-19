const co = require('co');
const mongoose = require('mongoose');
const ObjectId = mongoose.Types.ObjectId;

const Group = require('../models/group');
const ProblemList = require('../models/problem_list');

const OnError = require('./on_error');

function ProblemListCreate(req, res, next) {
    co(function* () {
        let user = req.session.user;
        let thisOwnerId = user._id;
        if (req.body.groupId) {
            let theGroup = yield Group.findById(req.body.groupId).exec();
            if (theGroup === null) {
                res.json({ code: 11 });
                return;
            }
            if (theGroup.members.some(v => v.userId.toString() === user._id && v.role === 'owner')) {
                thisOwnerId = req.body.groupId;
            } else {
                res.json({ code: 7 });
                return;
            }
        }
        let newProblemList = new ProblemList({
            ownerId: new ObjectId(thisOwnerId),
            name: (req.body.name || '').trim()
        });
        newProblemList = yield newProblemList.save();
        console.log(`new problemlist '${newProblemList.name}' created by ${user.username}`)
        res.json({
            code: 0,
            problemList: newProblemList
        });
    }).catch(OnError(req, res));
}

module.exports = ProblemListCreate;