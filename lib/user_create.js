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
        if (req.body.admin) { // [Deprecated] ADMIN
            console.log(`[Deprecated] post to 'system/administrator' instead.`);
            let admin = yield User.findOne({ admin: true }).exec();
            if (admin) {
                res.json({ code: 7 });
            } else {
                user.admin = true;
            }
        }
        // save user doc
        user = yield new User(user).save();
        user.password = undefined; // hide password
        // return to front-end
        res.json({ code: 0, user });
    }).catch(OnError(req, res));
}

module.exports = UserCreate;