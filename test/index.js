const co = require('co');

const testSystemInstall = require('./lib/system_install');
const testSystemUninstall = require('./lib/system_uninstall');

const testUserCreate = require('./lib/user_create');
const testUserLogin = require('./lib/user_login');

describe('[OrangeJuice]', function () {
    describe('[User Create]', testUserCreate);
    describe('[User Login]', testUserLogin);
    describe('[User Logout]', require('./lib/user_logout'));
    describe('[User Profile Retrieve]', require('./lib/user_profile_retrieve'));
    // describe('[User Group Create]', require('./lib/user_group_create'));
});