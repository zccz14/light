/**
 * 所有数据库模型
 * @module 
 * @author zccz14
 */

/**
 * @member System
 */
const System = require('./system');
/**
 * @member User
 */
const User = require('./user');
/**
 * @member Problem
 */
const Problem = require('./problem');
/**
 * @member ProblemList
 */
const ProblemList = require('./problem_list');
/**
 * @member Group
 */
const Group = require('./group');
/**
 * @member Submission
 */
const Submission = require('./submission');
/**
 * @member Model
 */

const Models = {System, User, Problem, ProblemList, Group, Submission};

module.exports = Models;