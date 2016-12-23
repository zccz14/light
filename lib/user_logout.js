/**
 * @module
 * @author zccz14 <zccz14@outlook.com>
 */
/**
 * @name UserLogout
 * @description 用户登出的中间件
 * @function
 * @param {Object} req - HTTPRequest
 * @param {Object} res - HTTPResponse
 * @param {Object} next - 下一个中间件
 * @returns {void}
 *  
 * @api {get} /user/sign-out 用户登出
 * @apiVersion 0.1.0
 * @apiName UserLogout
 * @apiGroup User
 * @apiPermission none
 * 
 * @apiSuccess {Number} code 0
 * 
 */

function UserLogout(req, res, next) {
  req.session.destroy(function(err) {
    if (err) throw err;
    res.json({code: 0});
  });
}

module.exports = UserLogout;