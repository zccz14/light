module.exports = require('express')
                     .Router()
                     // create a new problemlist
                     .post('/', require('../lib/require_login'))
                     .post('/', require('../lib/problem_list_create'))
                     // find available problemlist
                     .get('/', require('../lib/problem_list_retrieve'))

                     // find a problemlist
                     .get('/:problemListId', require('../lib/problem_list_detail'))
                     .post('/:problemListId/problem', require('../lib/problem_list_problem_create'))
                     // delete an existing problemlist

                     // update a problemlist======================
                     // add a problem into a problemlist
                     .put(':/_id', require('../lib/require_login'))
                     .put('/:_id', require('../lib/problem_list_update'))
                 // delete a problem from a problemlist

                 //