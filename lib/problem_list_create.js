/**
 * Problem List Create
 * Contributors: aak1247, zccz14
 * Description:
 *   创建一个题单
 * Request:
 *   body:
 *     name: 题单名 （可重复）
 *     groupId: 题单所有组的ID [optional]
 * Comment:
 *   用户创建一个题单，题单的所有者默认为该用户。
 *   用户也可以通过 groupId 直接将题单创建在其所有的 Group 下。
 * @module
 */
const co = require('co');

const {Group, ProblemList} = require('../models');

const OnError = require('./on_error');
/**
 * @api {post} /problem_list 创建题单
 * @apiVersion 0.1.0
 * @apiName ProblemListCreate
 * @apiGroup ProblemList
 * @apiPermission user
 * 
 * @apiParam {String} name 题单名
 * @apiParam {ObjectId} [groupId] 所有组ID
 * 
 * @apiSuccess {Number} code 0
 * @apiSuccess {Object} problemList 题单文档
 * 
 */
function ProblemListCreate(req, res) {
  co(function* () {
    let {user} = req.session;
    let {name = '', groupId} = req.body;
    name = name.toString().trim();
    let ownerId = user._id;
    if (groupId) {
      let theGroup = yield Group.findById(groupId).exec();
      if (theGroup === null) {
        res.json({ code: 11 });
        return;
      }
      if (theGroup.members.some(v => v.userId.toString() === user._id && v.role === 'owner')) {
        ownerId = groupId;
      } else {
        res.json({ code: 7 });
        return;
      }
    }
    let problemList = new ProblemList({ ownerId, name });
    problemList = yield problemList.save();
    res.json({ code: 0, problemList });
  }).catch(OnError(req, res));
}

module.exports = ProblemListCreate;