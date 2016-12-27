/**
 * @module
 * @author zccz14 <zccz14@outlook.com>
 */
const UserRouter = require('express').Router();

UserRouter.get('/', require('../lib/user_retrieve'));

UserRouter.get('/:userId', require('../lib/user_detail'));

UserRouter.post('/', require('../lib/user_create'));

UserRouter.post('/sign-in', require('../lib/user_login'));

UserRouter.get('/sign-out', require('../lib/user_logout'));

UserRouter.get('/profile', require('../lib/require_login'));
UserRouter.get('/profile', require('../lib/user_profile_detail'));

UserRouter.put('/role/:_id', require('../lib/require_login'));
UserRouter.put('/role/:_id', require('../lib/user_role_update'));

module.exports = UserRouter;