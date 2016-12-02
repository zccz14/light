const express = require('express');
const mongoose = require('mongoose');
const ObjectId = mongoose.Types.ObjectId;
const configuration = require('../config');
const AccessControl = require('./assess_control');
const User = require('../models/user');
const UserRole = require('../models/user_role');
// User CRUD API
module.exports = express.Router()
    // Create User (Sign Up)
    .post('/', (req, res, next) => {
        var username = (req.body.username || '').trim();
        var email = (req.body.email || '').trim();
        var password = req.body.password || '';
        // non-empty validate
        new User({ username, email, password }).save(function (err, user, num) {
            if (err) {
                if (err.errors) {
                    res.json({
                        code: 2,
                        errors: err.errors
                    });
                } else if (err.code === 11000) {
                    // email duplicated
                    res.json({
                        code: 3,
                        errors: err.errmsg
                    });
                } else {
                    res.json({ code: 1 });
                }
            } else {
                res.json({ code: 0 });
            }
        });
    })
    // Sign In
    .post('/sign-in', function (req, res, next) {
        var username = (req.body.username || '').trim();
        var password = req.body.password || '';
        User.findOne({ username }, function (err, user) {
            if (err) throw err;
            if (user && configuration.system.passwordHash.verify(password, user.password)) {
                req.session.user = user; // create cache
                res.json({ code: 0 });
            } else {
                res.json({ code: 5 });
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
        User.findById(req.session.user._id, (err, user) => {
            if (err) throw err;
            res.json({
                code: 0,
                body: user
            });
        });
    })
    // Update role name
    .put('/role/:_id', AccessControl.signIn)
    .put('/role/:_id', function (req, res, next) {
        var user = req.session.user;
        var userRoleId = req.params._id;
        var newName = (req.body.name || '').trim();
        var userRoles = user.roles;
        if (isItsRoleId) {
            User.update(
                {
                    _id: new ObjectId(user._id),
                    "roles._id": new ObjectId(userRoleId),
                },
                {
                    $set: { "roles.$.name": newName }
                },
                function (err, user) {
                    if (err) {
                        if (err.errors) {
                            res.json({
                                code: 2,
                                errors: err.errors
                            });
                        } else if (err.code == 11000) {
                            res.json({
                                code: 3,
                                errors: err.errmsg
                            });
                        } else {
                            res.json({
                                code: 1
                            });
                        }
                    } else {
                        req.session.user = user; // update cache
                        res.json({
                            code: 0
                        });
                    }
                })
        } else {
            res.json({
                code: 11
            });
        }
    })