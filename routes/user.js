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
    // non-empty validate
    if (!email) {
      res.json({
        code: 13,
        msg: 'expect the field(s) to be nonempty',
        body: 'email'
      });
      return;
    }
    if (!password) {
      res.json({
        code: 13,
        msg: 'expect the field to be nonempty',
        body: 'password'
      });
      return;
    }
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
    {
      let lowercaseLimit = configuration.user.password.minimumLowercaseLetter;
      let lowercases = password.match(/[a-z]/g);
      if (!lowercases || lowercases.length < lowercaseLimit) {
        res.json({
          code: 5,
          msg: `the password should contain at least ${lowercaseLimit} lowercase letter${lowercaseLimit > 1 ? 's' : ''}.`
        });
        return;
      }
    }
    // check the number of numberals in the password
    {
      let numberalLimit = configuration.user.password.minimumNumeral;
      let numberals = password.match(/\d/g);
      if (!numberals || numberals.length < numberalLimit) {
        res.json({
          code: 7,
          msg: `the password should contain at least ${numberalLimit} numberal${numberalLimit > 1 ? 's' : ''}.`
        });
        return;
      }
    }
    db.collection('users').insertOne({ email, password }, (err, user) => {
      if (err) {
        if (err.code == 11000) {
          // email duplicated
          res.json({
            code: 11,
            msg: 'the email has been used'
          });
          return;
        }
        throw err;
      }
      res.json({
        code: 0,
        msg: 'ok',
      });
    });
  })
  // Sign In
  .post('/sign-in', function (req, res, next) {
    var email = req.body.email;
    var password = req.body.password;
    if (!email) {
      res.json({
        code: 13,
        msg: 'email'
      });
      return;
    }
    if (!IsEmail.validate(email)) {
      res.json({
        code: 2,
        msg: 'illegal email'
      });
      return;
    }
    if (!password) {
      res.json({
        code: 13,
        msg: 'password'
      });
      return;
    }
    db.collection('users').findOne({ email }, function (err, user) {
      if (err) throw err;
      if (user) {
        if (user.password === password) {
          res.json({
            code: 0
          });
        } else {
          res.json({
            code: 17
          });
        }
      } else {
        res.json({
          code: 19
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