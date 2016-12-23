module.exports = require('express')
                     .Router()
                     // query user
                     .get('/', require('../lib/user_retrieve'))
                     // Create User (Sign Up)
                     .post('/', require('../lib/user_create'))
                     // Sign In
                     .post('/sign-in', require('../lib/user_login'))
                     // Sign Out
                     .get('/sign-out', require('../lib/user_logout'))
                     // Retrieve User Profile
                     .get('/profile', require('../lib/require_login'))
                     .get('/profile', require('../lib/user_profile_detail'))
                     // Update role name
                     .put('/role/:_id', require('../lib/require_login'))
                     .put('/role/:_id', require('../lib/user_role_update'))