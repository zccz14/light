module.exports = require('express').Router()
    // Admin API
    .use('/', require('../lib/require_administrator'))
    // create a problem
    .post('/', require('../lib/problem_create'))
    // get problem list
    .get('/', require('../lib/problem_retrieve'))
    // update a problem
    .put('/:_id', require('../lib/problem_update'))
    // delete a problem
    .delete('/:_id', require('../lib/problem_delete'))