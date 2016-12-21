const co = require('co');
const mongoose = require('mongoose');
const ObjectId = mongoose.Types.ObjectId;

const User = require('../models/user');
const System = require('../models/system');

const OnError = require('../lib/on_error');

function SystemAdministratorCreate(req, res, next) {
    co(function* () {
        let user = req.session.user;
        let userId = req.body.userId || user._id;
        let system = yield System.findOneAndUpdate({}, {
            $addToSet: {
                administrators: userId
            }
        }, { new: true }).exec();
        res.json({ code: 0, system });
    }).catch(OnError(req, res));
}

module.exports = SystemAdministratorCreate;