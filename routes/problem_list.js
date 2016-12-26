/**
 * @module
 * @author zccz14 <zccz14@outlook.com>
 */

const ProblemListRouter = require('express').Router();

ProblemListRouter.post('/', require('../lib/require_login'));
ProblemListRouter.post('/', require('../lib/problem_list_create'));

ProblemListRouter.get('/', require('../lib/problem_list_retrieve'));

ProblemListRouter.get('/:problemListId', require('../lib/problem_list_detail'));

ProblemListRouter.post('/:problemListId/problem', require('../lib/problem_list_problem_create'));

ProblemListRouter.put(':/_id', require('../lib/require_login'));
ProblemListRouter.put('/:_id', require('../lib/problem_list_update'));

module.exports = ProblemListRouter;
