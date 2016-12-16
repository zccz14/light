const co = require('co');
const express = require('express');
const AccessControl = require('./assess_control');
const User = require('../models/user');
const Group = require('../models/group');
const OnError = require('./on_error');
const mongoose = require('mongoose');
const ObjectId = mongoose.Types.ObjectId;

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
                code: 0,
                group: newGroup
            });
        }).catch(OnError(res));
    })

    //delete an existing group
    .delete('/:_id', function (req, res, next) {
        res.json();
    })

    //update an existing group
    .put('/:_id', AccessControl.signIn)
    .put('/:_id', function (req, res, next) {
        co(function* () {
            let user = req.session.user;
            let groupId = req.params._id;
            let newName = (req.body.name || '').trim();
            group = yield Group.update(
                {
                    "_id": new ObjectId(groupId),
                    "members.userId": new ObjectId(user._id),
                    "members.role": 'owner'
                },
                {
                    $set: {
                        "name": newName
                    }
                }
            ).exec();
            if (group.nModified === 1) {
                res.json({
                    code: 0
                });
            } else {
                res.json({
                    code: 11
                });
            }
        }).catch(OnError(res));
    })

    //find a exist group
    .get('/', function (req, res, next) {

        res.json();
    })