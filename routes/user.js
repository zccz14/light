const co = require('co');
const express = require('express');
const mongoose = require('mongoose');
const ObjectId = mongoose.Types.ObjectId;
const configuration = require('../config');
const AccessControl = require('./assess_control');
const User = require('../models/user');
const UserRole = require('../models/user_role');
const OnError = require('./on_error');
// User CRUD API
module.exports = express.Router()
    // Create User (Sign Up)
    .post('/', (req, res, next) => {
        co(function* () {
            let newUser = new User({
                username: (req.body.username || '').trim(),
                email: (req.body.email || '').trim(),
                password: req.body.password || ''
            });
            newUser = yield newUser.save();
            res.json({ code: 0 });
        }).catch(OnError(res));
    })
    // Sign In
    .post('/sign-in', function (req, res, next) {
        co(function* () {
            let username = (req.body.username || '').trim();
            let password = req.body.password || '';
            let user = yield User.findOne({ username }).exec();
            if (user) {
                if (configuration.system.passwordHash.verify(password, user.password)) {
                    req.session.user = user;
                    res.json({ code: 0 });
                } else {
                    res.json({
                        code: 5,
                        msg: 'wrong username or password'
                    });
                }
            } else {
                res.json({
                    code: 11,
                    msg: 'user not found'
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
        co(function* () {
            let user = req.session.user;
            user = yield User.findById(user._id).exec();
            res.json({
                code: 0,
                body: user
            });
        });
    })
    // Update role name
    .put('/role/:_id', AccessControl.signIn)
    .put('/role/:_id', function (req, res, next) {
        co(function* () {
            let user = req.session.user;
            user = yield User.update(
                {
                    _id: new ObjectId(user._id),
                    "roles._id": new ObjectId(req.params._id),
                },
                {
                    $set: { "roles.$.name": (req.body.name || '').trim() }
                }
            ).exec();
            req.session.user = user;
            res.json({ code: 0 });
        }).catch(OnError(res));
    })