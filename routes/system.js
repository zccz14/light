const co = require('co');
const express = require('express');
const mongoose = require('mongoose');
const ObjectId = mongoose.Types.ObjectId;
const request = require('superagent');

const User = require('../models/user');
const Administrator = require('../models/administrator');
const OnError = require('../lib/on_error');
const AccessControl = require('../lib/access_control');

module.exports = express.Router()
    // install the system
    .post('/install', function (req, res, next) {
        co(function* () {
            // create root
            let root = {
                username: (req.body.username || '').trim(),
                email: (req.body.email || '').trim(),
                password: req.body.password || ''
            };
            yield request
                .post('/user')
                .send(root)
            // save root
            root = yield new User(root).save();
            yield new Administrator({ userId: root._id }).save();
            res.json({ code: 0 })
        });
    })
    // Let a user to be system administrator
    .post('/administrator', AccessControl.signIn)
    .post('/administrator', function (req, res, next) {
        co(function* () {
            let user = req.session.user;
            let userId = req.body.userId || user._id;
            if (!ObjectId.isValid(userId)) {
                res.json({ code: 2 });
                return;
            }
            let admins = yield Administrator.find({}).exec();
            if (admins.length === 0 || admins.some(v => v.userId.toString() === user._id)) {
                let newAdmin = new Administrator({
                    userId: new ObjectId(userId)
                });
                newAdmin = yield newAdmin.save();
                res.json({ code: 0 });
            } else {
                res.json({ code: 7 });
            }
        }).catch(OnError(res));
    })
