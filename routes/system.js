const co = require('co');
const express = require('express');
const mongoose = require('mongoose');
const ObjectId = mongoose.Types.ObjectId;

const User = require('../models/user');
const Administrator = require('../models/administrator');
const OnError = require('./on_error');
const AccessControl = require('./access_control');

module.exports = express.Router()
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
            if (admins.length === 0 || admins.some(v => v.userId === user._id)) {
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
