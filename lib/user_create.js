const co = require('co');

const User = require('../models/user');

const OnError = require('./on_error');
const config = require('../config');
/**
 * @api {post} /user Create User
 * @apiVersion 0.1.0
 * @apiName UserCreate
 * @apiGroup User
 * 
 * @apiParam {String} username 用户名
 * @apiParam {String} password 密码
 * @apiParam {String} email 邮件地址
 * 
 * @apiSuccess {Number} code 0
 * @apiSuccess {Object} user 用户文档
 * 
 *  
 * @apiPermission none
 */
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