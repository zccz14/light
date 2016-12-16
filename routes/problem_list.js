const co = require('co');
const express = require('express');
const AccessControl = require('./assess_control');
const User = require('../models/user');
const ProblemList = require('../models/problem_list');
const OnError = require('./on_error');
const mongoose = require('mongoose');
const ObjectId = mongoose.Types.ObjectId;

module.exports = express.Router()
  //访问权限控制考虑采用group和role
  //不过还没加==


  //create a new problemlist
  .post('/', AccessControl.signIn)
  .post('/', function (req, res, next) {
    co(function* () {
      var user = req.session.user;
      let name = (req.body.listName || '').trim();
      var newProblemList = new ProblemList({
        listName,
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
  .get('/:problemListName', function (req, res, next) {
    co(function* () {
      let thisProblemListName = req.params.problemListName;
      //let problemList = 
      ProblemList.find(({ listName: thisProblemListName }), function (err, docs) {
        if (err) {
          res.json({
            code: 11,
            msg: 'Problem list not found'
          })
        }
        else return res.json({
          docs
        })
      });
    }).catch(OnError(res));
  })

  //delete an existing problemlist




  //update a problemlist
  //add a problem into a problemlist
  .put('/:problemListName', function (req, res, next) {
    co(function* () {
      let thisProblemName = (req.body.problemName).trim();
      let thisProblemListName = req.params.problemListName;
      problemList = yield ProblemList.update(

        {
          listName: thisProblemListName
        },
        {
          $push: {
            Problem: [{
              problemName: thisProblemName
              //insert promlem content here
            }]
          }
        }
      )
      res.json(problemList);
    }).catch(OnError(res));
  })