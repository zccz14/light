const co = require('co');
const express = require('express');
const mongoose = require('mongoose');
const ObjectId = mongoose.Types.ObjectId;
const configuration = require('../config');
const AccessControl = require('./access_control');
const User = require('../models/user');
const UserRole = require('../models/user_role');
const OnError = require('./on_error');
// User CRUD API
module.exports = express.Router()
    // Create User (Sign Up)
    .post('/', (req, res, next) => {
        co(function* () {
            let user = {
                username: (req.body.username || '').trim(),
                email: (req.body.email || '').trim(),
                password: req.body.password || ''
            };
            if (req.body.admin) { // [Deprecated] ADMIN
                console.log(`[Deprecated] post to 'system/administrator' instead.`);
                let admin = yield User.findOne({ admin: true }).exec();
                if (admin) {
                    res.json({ code: 7 });
                } else {
                    user.admin = true;
                }
            }
            user = yield new User(user).save();
            user.password = undefined; // hide password
            res.json({ code: 0, user });
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
                    user.password = undefined;
                    req.session.user = user;
                    res.json({ code: 0, user });
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
            res.json({ code: 0, user });
        }).catch(OnError(res));
    })
    // query user
    .get('/', function (req, res, next) {
        co(function* () {
            let limit = parseInt(req.query.limit) || 15;
            let skip = parseInt(req.query.skip) || 0;
            delete req.query.limit;
            delete req.query.skip;
            delete req.query.password;
            let users = yield User.find(req.query, { password: 0 }).limit(limit).skip(skip).exec();
            res.json({ code: 0, users });
        }).catch(OnError(res));
    })