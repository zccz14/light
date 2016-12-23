/**
 * @module
 * @author zccz14 <zccz14@outlook.com>
 */
const co = require('co');

const User = require('../models/user');

const OnError = require('./on_error');

/**
 * @name UserProfileDetail
 * @description 获取用户个人信息的中间件
 * @function
 * @param {Object} req - HTTPRequest
 * @param {Object} res - HTTPResponse
 * @param {Object} next - 下一个中间件
 * @returns {void}
 *  
 * @api {get} /user/profile 用户个人信息
 * @apiVersion 0.1.0
 * @apiName UserProfile
 * @apiGroup User
 * @apiPermission user
 * 
 * @apiSuccess {Number} code 0
 * @apiSuccess {User} user 用户文档(个人信息)
 * 
 */

function UserProfileDetail(req, res, next) {
  co(function * () {
    let user = req.session.user;
    user = yield User.findById(user._id).exec();
    res.json({
      code: 0,
      user
    });
  });
}

module.exports = UserProfileDetail;