const express = require('express');
const ObjectId = require('mongodb').ObjectId;
const IsEmail = require('isemail');
const configuration = require('../config');

var db = require('../db');
// User CRUD API
module.exports = express.Router()
  // Create User (Sign Up)
  .post('/', (req, res, next) => {
    var email = req.body.email;
    var password = req.body.password;
    // validate email and password format
    if (!IsEmail.validate(email)) {
      res.json({
        code: 2,
        msg: 'the email is illegal',
        body: {}
      });
      return;
    }
    // password length check
    var lengthLimit = configuration.user.password.minimumLength;
    if (password.length < lengthLimit) {
      res.json({
        code: 3,
        msg: `the password's length should be at least ${lengthLimit}`
      });
      return;
    }
    // check the number of letters in the password
    var lowercaseLimit = configuration.user.password.minimumLowercaseLetter;
    if (password.match(/[a-z]/g).length < lowercaseLimit) {
      res.json({
        code: 5,
        msg: `the password should contain at least ${lowercaseLimit} lowercase letter${lowercaseLimit > 1 ? 's' : ''}.`
      });
      return;
    }
    // check the number of numberals in the password
    var numberalLimit = configuration.user.password.minimumNumeral;
    if (password.match(/\d/g).length < numberalLimit) {
      res.json({
        code: 7,
        msg: `the password should contain at least ${numberalLimit} numberal${numberal > 1 ? 's' : ''}.`
      });
      return;
    }
    db.collection('users').findOne({ email }, { field: email }, (err, user) => {
      if (err) throw err;
      if (user) {
        res.json({
          code: 11,
          msg: 'the email has been used'
        });
      } else {
        db.collection('users').insertOne({ email, password }, (err, cursor) => {
          if (err) throw err;
          res.json({
            code: 0,
            msg: 'ok',
          });
        });
      }
    })
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
    if (ObjectId.isValid(req.params._id) === false) {
      res.json({
        code: 1,
        msg: 'invalid user id',
        body: {}
      });
      return;
    }
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
    if (ObjectId.isValid(req.params._id) === false) {
      res.json({
        code: 1,
        msg: 'invalid user id',
        body: {}
      });
      return;
    }
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