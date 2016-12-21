const co = require('co');
const mongoose = require('mongoose');
const ObjectId = mongoose.Types.ObjectId;

const User = require('../models/user');
const Administrator = require('../models/administrator');

const OnError = require('../lib/on_error');

function SystemAdministratorCreate(req, res, next) {
    co(function* () {
        let user = req.session.user;
        let userId = req.body.userId || user._id;
        if (!ObjectId.isValid(userId)) {
            res.json({ code: 2 });
            return;
        }
        let admins = yield Administrator.find({}).exec();
        if (admins.length === 0 || admins.some(v => v.userId.toString() === user._id)) {
            let newAdmin = new Administrator({
                userId: new ObjectId(userId)
            });
            newAdmin = yield newAdmin.save();
            res.json({ code: 0 });
        } else {
            res.json({ code: 7 });
        }
    }).catch(OnError(req, res));
}

module.exports = SystemAdministratorCreate;