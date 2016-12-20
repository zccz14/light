const co = require('co');

const User = require('../models/user');

const OnError = require('./on_error');

function UserCreate(req, res, next) {
    co(function* () {
        // combine user doc
        let user = {
            username: (req.body.username || '').trim(),
            email: (req.body.email || '').trim(),
            password: req.body.password || ''
        };
        // save user doc
        user = yield new User(user).save();
        user.password = undefined; // hide password
        // return to front-end
        res.json({ code: 0, user });
    }).catch(OnError(req, res));
}

module.exports = UserCreate;