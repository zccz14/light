const co = require('co');

const User = require('../models/user');

const OnError = require('./on_error');
const config = require('../config');

function UserCreate(req, res, next) {
  co(function * () {
    // combine user doc
    let user = {
      username: (req.body.username || '').trim(),
      email: (req.body.email || '').trim(),
      password: req.body.password || ''
    };
    // save user doc
    user = yield new User(user).save();
    user.password = config.system.passwordHash.store(user.password);
    user = yield user.save();
    user.password = undefined;  // hide password
    // return to front-end
    res.json({code: 0, user});
  }).catch(OnError(req, res));
}

module.exports = UserCreate;