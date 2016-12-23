/**
 * @module
 * @author zccz14 <zccz14@outlook.com>
 */
const co = require('co');

const User = require('../models/user');

const OnError = require('./on_error');
const config = require('../config');
/**
 * @name UserCreate
 * @description 创建用户的中间件
 * @function
 * @param {Object} req - HTTPRequest
 * @param {Object} res - HTTPResponse
 * @param {Object} next - 下一个中间件
 * @returns {void}
 * 
 * @api {post} /user 创建用户
 * @apiVersion 0.1.0
 * @apiName UserCreate
 * @apiGroup User
 * @apiPermission none
 * 
 * @apiParam {String} username 用户名
 * @apiParam {String} password 密码
 * @apiParam {String} email 邮件地址
 * 
 * @apiSuccess {Number} code 0
 * @apiSuccess {Object} user 用户文档
 * 
 * @apiError (Error 2 - ValidationError) {Object} require 必填字段缺失
 * @apiError (Error 3 - DataDuplicated) {Object} username 用户名重复
 * @apiError (Error 3 - DataDuplicated) {Object} email 电子邮件重复
 * 
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