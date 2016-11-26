const express = require('express');
const configuration = require('../config');
const AccessControl = require('./assess_control');
const User = require('../models/user');
// User CRUD API
module.exports = express.Router()
  // Create User (Sign Up)
  .post('/', (req, res, next) => {
    var email = req.body.email || '';
    var password = req.body.password || '';
    // non-empty validate
    new User({ email, password }).save(function (err, user, num) {
      if (err) {
        if (err.errors) {
          res.json({
            code: 2,
            msg: 'validation failed',
            errors: err.errors
          });
        } else if (err.code === 11000) {
          // email duplicated
          res.json({
            code: 3,
            msg: 'duplicated',
            errors: ['email']
          });
        } else {
          console.log(err); // console for debug
          res.json({
            code: 1,
          });
        }
      } else {
        res.json({
          code: 0
        });
      }
    });
  })
  // Sign In
  .post('/sign-in', function (req, res, next) {
    var email = req.body.email || '';
    var password = req.body.password || '';
    User.findOne({ email }, function (err, user) {
      if (err) throw err;
      if (user && configuration.system.passwordHash.verify(password, user.password)) {
        req.session.userId = user._id;
        res.json({
          code: 0,
        });
      } else {
        res.json({
          code: 5
        });
      }
    });
  })
  // Sign Out
  .get('/sign-out', function (req, res, next) {
    req.session.destroy(function (err) {
      if (err) throw err;
      res.json({
        code: 0
      });
    });
  })
  // Retrieve User Profile
  .get('/profile', AccessControl.signIn)
  .get('/profile', function (req, res, next) {
    User.findById(req.session.userId, (err, user) => {
      if (err) throw err;
      res.json({
        code: 0,
        body: user
      });
    });
  })
