module.exports = require('express')
                     .Router()
                     // create a problem
                     .post('/', require('../lib/problem_create'))
                     // get problem list
                     .get('/', require('../lib/problem_retrieve'))
                     // update a problem
                     .put('/:problemId', require('../lib/problem_update'))
                     // delete a problem
                     .delete('/:problemId', require('../lib/problem_delete'))
                     .get('/:problemId', require('../lib/require_login'))
                     .get('/:problemId', require('../lib/problem_detail'))