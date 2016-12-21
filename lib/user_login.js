const co = require('co');

const User = require('../models/user');

const OnError = require('./on_error');
const configuration = require('../config');

function UserLogin(req, res, next) {
    co(function* () {
        // combine user doc
        let username = (req.body.username || '').trim();
        let password = req.body.password || '';
        let user = yield User.findOne({ username }).exec();
        if (user) {
            if (configuration.system.passwordHash.verify(password, user.password)) {
                user.password = undefined;
                req.session.user = user;
                res.json({ code: 0, user });
            } else {
                res.json({
                    code: 5,
                    msg: 'wrong username or password'
                });
            }
        } else {
            res.json({
                code: 11,
                msg: 'user not found'
            });
        }
    }).catch(OnError(req, res));
}

module.exports = UserLogin;