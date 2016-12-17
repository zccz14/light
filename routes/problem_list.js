const co = require('co');
const express = require('express');
const AccessControl = require('./access_control');
const User = require('../models/user');
const ProblemList = require('../models/problem_list');
const Problem = require('../models/problem')
const OnError = require('./on_error');
const mongoose = require('mongoose');
const UserRole = require('../models/user_role')
const ObjectId = mongoose.Types.ObjectId;

module.exports = express.Router()
  //访问权限控制考虑采用group和role
  //不过还没加==


  //create a new problemlist
  .post('/', AccessControl.signIn)
  .post('/', function (req, res, next) {
    co(function* () {
      let user = req.session.user;
      let thisOwnerId = user._id;
      if (req.body.groupId) {
        let theGroup = yield Group.findById(req.body.groupId).exec();
        if (theGroup === null) {
          res.json({ code: 11 });
          return;
        }
        if (theGroup.members.some(v => v.userId === user._id && v.role === 'owner')) {
          thisOwnerId = req.body.groupId;
        } else {
          res.json({ code: 7 });
          return;
        }
      }
      let newProblemList = new ProblemList({
        ownerId: new ObjectId(thisOwnerId),
        name: (req.body.name || '').trim()
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
  .get('/:_id', function (req, res, next) {
    co(function* () {
      let problemList = yield ProblemList.findById(req.params._id).exec();
      if (problemList === null) {
        res.json({ code: 11 });
        return;
      }
      problemList.problems = yield Problem.find({
        _id: {
          $in: problemList.problems.map(v => new ObjectId(v))
        }
      }).exec();
      res.json({
        code: 0,
        problemList
      });
    }).catch(OnError(res));
  })

  //delete an existing problemlist




  //update a problemlist======================
  //add a problem into a problemlist
  .put(':/_id', AccessControl.signIn)
  .put('/:_id', function (req, res, next) {
    co(function* () {
      let user = req.session.user;
      let thisProblemTitle = req.body.problemTitle;
      let thisProblemDescription = req.body.problemDescription;
      let thisProblemListId = req.params._id;
      var isOwner = flase;
      let thisProblemList = yield ProblemList.findById(new ObjectId(thisProblemListId).exec())
      if (User._id === thisProblemList.ownerId) {
        isOwner = true;
      }
      let judgeGroup = yield User.find({
        '_id': new ObjectId(user._id),
        'roles.$.role': 'owner',
        'roles.$.group': thisProblemList._id
      })
      if (judgeGroup) {
        isOwner = true;
      }
      if (isOwner) {
        problemList = yield ProblemList.update(
          {
            "_id": new Object(thisProblemListId)
          },
          {
            $push: {
              Problem: [{
                title: thisProblemTitle,
                description: thisProblemDescription
                //insert promlem content here
              }]
            }
          }
        )
        res.json({
          code: 0,
          problemList
        });
      } else {
        res.json({
          code: 7,
          msg: "Authentication denied"
        })
      }

    }).catch(OnError(res));
  })
  //delete a problem from a problemlist

  //