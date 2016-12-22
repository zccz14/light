/**
 * Model Index
 * Author: zccz14
 * Date: 2016-12-22 19:22:01
 */
const System = require('./system');
const User = require('./user');
const Problem = require('./problem');
const ProblemList = require('./problem_list');
const Group = require('./group');
const Submission = require('./submission');

const Models = {System, User, Problem, ProblemList, Group, Submission};

module.exports = Models;