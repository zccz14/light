const co = require('co');
const express = require('express');
const AccessControl = require('./assess_control');
const User = require('../models/user');
const Group = require('../models/group');

module.exports = express.Router()
    // create a group
    .post('/', AccessControl.signIn)
    .post('/', function (req, res, next) {
        co(function* () {
            var user = req.session.user;
            var name = (req.body.name || '').trim();
            var newGroup = new Group({
                name,
                members: [
                    {
                        userId: user._id,
                        name: user.username,
                        role: 'owner'
                    }
                ]
            });
            newGroup = yield newGroup.save();
            console.log(`new group '${newGroup.name}' created by ${user.username}`);
            user = yield User.findByIdAndUpdate(user._id, {
                $push: {
                    roles: {
                        name: user.username,
                        role: 'owner',
                        group: newGroup._id
                    }
                }
            }).exec();
            req.session.user = user;
            res.json({
                code: 0
            });
        }).catch(function (err) {
            if (err.errors) {
                res.json({
                    code: 2,
                    errors: err.errors
                });
            } else if (err.code === 11000) {
                res.json({
                    code: 3,
                    errors: err.errmsg
                });
            } else {
                res.json({
                    code: 1
                });
            }
        })
    })