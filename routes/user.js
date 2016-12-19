const express = require('express');
const AccessControl = require('./access_control');
// User CRUD API
module.exports = express.Router()
    // query user
    .get('/', require('../lib/user_retrieve'))
    // Create User (Sign Up)
    .post('/', require('../lib/user_create'))
    // Sign In
    .post('/sign-in', require('../lib/user_login'))
    // Sign Out
    .get('/sign-out', require('../lib/user_logout'))
    // Retrieve User Profile
    .get('/profile', AccessControl.signIn)
    .get('/profile', require('../lib/user_profile_retrieve'))
    // Update role name
    .put('/role/:_id', AccessControl.signIn)
    .put('/role/:_id', require('../lib/user_role_update'))