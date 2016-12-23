/**
*@api{post} /group
*@apiVersion 0.1.0
*@apiName GroupCreate
*@apiGroup UserGroup
*@apiPermission none
*
*
*@apiParam {String} name 组名
*@apiParam {GroupRole[]} members 组内成员 
*
* @apiSuccess {Number} code 0
* @apiSuccess {Object} user 用户文档
*/


const co = require('co');

const User = require('../models/user');
const Group = require('../models/group');

const OnError = require('./on_error');

function UserGroupCreate(req, res, next) {
  co(function * () {
    var user = req.session.user;
    var name = (req.body.name || '').trim();
    var newGroup = new Group({
      name,
      members: [{userId: user._id, name: user.username, role: 'owner'}]
    });
    newGroup = yield newGroup.save();
    console.log(`new group '${newGroup.name}' created by ${user.username}`);
    user = yield User.findByIdAndUpdate(user._id,
                                        {
                                          $push: {
                                            roles: {
                                              name: user.username,
                                              role: 'owner',
                                              group: newGroup._id
                                            }
                                          }
                                        })
               .exec();
    req.session.user = user;
    res.json({code: 0, group: newGroup});
  }).catch(OnError(req, res));
}

module.exports = UserGroupCreate;