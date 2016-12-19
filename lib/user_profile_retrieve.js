const co = require('co');

const User = require('../models/user');

const OnError = require('./on_error');

function UserProfileRetrieve(req, res, next) {
    co(function* () {
        let user = req.session.user;
        user = yield User.findById(user._id).exec();
        res.json({
            code: 0,
            body: user
        });
    });
}

module.exports = UserProfileRetrieve;