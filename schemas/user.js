const mongoose = require('mongoose');
const Schema = mongoose.Schema;
const IsEmail = require('isemail');
const config = require('../config');

const UserRoleSchema = require('./user_role');
const ProblemSchema = require('./problem');

const UserSchema = new Schema({
    username: {
        type: String,
        required: true,
        unique: true
    },
    email: {
        type: String,
        required: true,
        validate: {
            validator: IsEmail.validate,
            message: 'email illegal'
        },
        unique: true
    },
    password: {
        type: String,
        required: true,
        minlength: 7,
        validate: {
            validator: function (v) {
                return v.match(/[a-z]/) && v.match(/\d/);
            },
            message: 'password illegal'
        }
    },
    roles: [UserRoleSchema],
    admin: {
        type: Boolean,
        default: false
    },
    InvitedTo: [Schema.Types.ObjectId],
    problems: [ProblemSchema]
});

module.exports = UserSchema;