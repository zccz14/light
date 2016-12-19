module.exports = require('express').Router()
    .use(require('../lib/require_login'))
    // create a group
    .post('/', require('../lib/user_group_create'))
    //update an existing group
    //change group name
    .put('/:_id', require('../lib/user_group_update'))

    // invite some members
    .post('/:_id/invite', require('../lib/user_group_invite'))

    // accept some invitations
    .post('/:_id/accept', require('../lib/user_group_accept'))