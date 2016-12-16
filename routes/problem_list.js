const co = require('co');
const express = require('express');
const AccessControl = require('./assess_control');
const User = require('../models/user');
const ProblemList = require('../models/problem_list');
const OnError = require('./on_error');
const mongoose = require('mongoose');
const ObjectId = mongoose.Types.ObjectId;

module.exports = express.Router()
  //create a new problemlist
  .post('/', AccessControl.signIn)
  .post('/', function (req, res, next) {
    co(function* () {
      let user = req.session.user;
      let name = (req.body.listName || '').trim();
      var newProblemList = new ProblemList({
        name,
        problems: null
      });
      newProblemList = yield newProblemList.save();
      console.log(`new problemlist '${newProblemList.name}' created by ${user.username}`)
      res.json({
        code: 0,
        problemList: newProblemList
      });
    }).catch(OnError(res));
  })

  //find a problemlist
  .get('/', function (req, res, next) {
    co(function* () {
      let listname = (req.body.listname || '').trim();

    }).catch(OnError(res));
  })

  //delete an existing problemlist




  //update a problemlist
  //add a problem into a problemlist
  .put('/', function (req, res, next) {
    co(function* () {
      let problemName = (req.body.problemName).trim();


    }).catch(OnError(res));
  })