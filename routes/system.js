module.exports = require('express').Router()
    .post('/', require('../lib/system_install'))
    .delete('/', require('../lib/system_uninstall'))
    // grant system administrator
    .post('/administrator', require('../lib/require_login'))
    .post('/administrator', require('../lib/system_administrator_create'))