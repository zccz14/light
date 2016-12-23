const co = require('co');

const User = require('../models/user');

const OnError = require('./on_error');

function UserProfileDetail(req, res, next) {
  co(function * () {
    let user = req.session.user;
    user = yield User.findById(user._id).exec();
    res.json({
      code: 0,
      body: user,  // [deprecated]
      user
    });
  });
}

module.exports = UserProfileDetail;