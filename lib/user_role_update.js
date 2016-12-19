const co = require('co');
const ObjectId = require('mongoose').Types.ObjectId;

const User = require('../models/user');

const OnError = require('./on_error');

function UserRoleUpdate(req, res, next) {
    co(function* () {
        let user = req.session.user;
        user = yield User.update(
            {
                _id: new ObjectId(user._id),
                "roles._id": new ObjectId(req.params._id),
            },
            {
                $set: { "roles.$.name": (req.body.name || '').trim() }
            }
        ).exec();
        req.session.user = user;
        res.json({ code: 0, user });
    }).catch(OnError(res));
}

module.exports = UserRoleUpdate;