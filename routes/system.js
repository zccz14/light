/**
 * @module
 * @author zccz14 <zccz14@outlook.com>
 */
const SystemRouter = require('express').Router();

SystemRouter.post('/', require('../lib/system_install'));

SystemRouter.delete('/', require('../lib/system_uninstall'));

SystemRouter.post('/administrator', require('../lib/require_login'));
SystemRouter.post('/administrator', require('../lib/system_administrator_create'));

module.exports = SystemRouter;