/**
 * @module
 * @author zccz14 <zccz14@outlook.com>
 */
const GroupRouter = require('express').Router();

GroupRouter.use(require('../lib/require_login'));

GroupRouter.post('/', require('../lib/user_group_create'));

GroupRouter.put('/:_id', require('../lib/user_group_update'));

GroupRouter.post('/:_id/invite', require('../lib/user_group_invite'));

GroupRouter.post('/:_id/accept', require('../lib/user_group_accept'));

module.exports = GroupRouter;