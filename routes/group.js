const co = require('co');
const mongoose = require('mongoose');
const ObjectId = mongoose.Types.ObjectId;
const express = require('express');
const AccessControl = require('./access_control');
const User = require('../models/user');
const Group = require('../models/group');
const OnError = require('./on_error');


module.exports = express.Router()
    // create a group
    .post('/', AccessControl.signIn)
    .post('/', require('../lib/user_group_create'))

    //update an existing group
    //change group name
    .put('/:_id', AccessControl.signIn)
    .put('/:_id', require('../lib/user_group_update'))

    // invite some members
    .post('/:_id/invite', AccessControl.signIn)
    .post('/:_id/invite', require('../lib/user_group_invite'))

    // accept some invitations
    .post('/:_id/accept', AccessControl.signIn)
    .post('/:_id/accept', require('../lib/user_group_accept'))