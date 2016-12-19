const co = require('co');
const express = require('express');
const mongoose = require('mongoose');
const ObjectId = mongoose.Types.ObjectId;
const AccessControl = require('./access_control');
const Problem = require('../models/problem');
const OnError = require('./on_error');

module.exports = express.Router()
    // Admin API
    .use('/', AccessControl.admin)
    // create a problem
    .post('/', require('../lib/problem_create'))
    // get problem list
    .get('/', require('../lib/problem_retrieve'))
    // update a problem
    .put('/:_id', require('../lib/problem_update'))
    // delete a problem
    .delete('/:_id', require('../lib/problem_delete'))