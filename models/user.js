const mongoose = require('mongoose');
const Schema = mongoose.Schema;
const IsEmail = require('isemail');
const config = require('../config');
const UserRoleSchema = require('./user_role');

var UserSchema = new Schema({
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
    roles: [UserRoleSchema]
});

UserSchema.post('validate', function (user) {
    // encryptoed after validation
    user.password = config.system.passwordHash.store(user.password);
});
const User = mongoose.model('user', UserSchema);
module.exports = User;