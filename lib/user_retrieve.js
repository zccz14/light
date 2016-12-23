const co = require('co');

const User = require('../models/user');

const OnError = require('./on_error');

function UserRetrieve(req, res, next) {
  co(function * () {
    let limit = parseInt(req.query.limit) || 15;
    let skip = parseInt(req.query.skip) || 0;
    delete req.query.limit;
    delete req.query.skip;
    delete req.query.password;
    let users = yield User.find(req.query, {password: 0})
                    .limit(limit)
                    .skip(skip)
                    .exec();
    res.json({code: 0, users});
  }).catch(OnError(req, res));
}

module.exports = UserRetrieve;