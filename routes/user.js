const express = require('express');
const ObjectId = require('mongodb').ObjectID;
var db = require('../db');
// User CRUD API
module.exports = express.Router()
  // Create User
  .post('/', (req, res, next) => {
    db.collection('users').insertOne(req.body, (err, cursor) => {
      if (err) throw err;
      res.json({
        code: 0,
        msg: 'ok',
        body: req.body
      });
    });
  })
  // Retrieve Users
  .get('/', (req, res, next) => {
    db.collection('users').find(req.query).toArray((err, users) => {
      if (err) throw err;
      res.json({
        code: 0,
        msg: 'ok',
        body: users
      });
    });
  })
  // Update User
  .put('/:_id', (req, res, next) => {
    db.collection('users').findOneAndUpdate({
      _id: new ObjectId(req.params._id)
      // this would crash when _id is illegal
    }, { $set: req.body }, (err, result) => {
      if (err) throw err;
      if (result.value) {
        res.json({
          code: 0,
          msg: 'ok',
          body: result
        });
      } else {
        res.json({
          code: 1,
          msg: 'user not found',
          body: result
        })
      }
    });
  })
  // Delete User
  .delete('/:_id', (req, res, next) => {
    db.collection('users').findOneAndDelete({
      _id: new ObjectId(req.params._id)
      // this would crash when _id is illegal
    }, (err, result) => {
      if (err) throw err;
      if (result.value) {
        res.json({
          code: 0,
          msg: 'ok',
          body: result
        });
      } else {
        res.json({
          code: 1,
          msg: 'user not found',
          body: result
        })
      }
    })
  })