const co = require('co');

describe('[OrangeJuice]', function () {
    describe('[System Install]', require('./specs/system_install'));
    describe('[System Uninstall]', require('./specs/system_uninstall'));
    // describe('[User Create]', testUserCreate);
    // describe('[User Login]', testUserLogin);
    // describe('[User Logout]', require('./lib/user_logout'));
    // describe('[User Profile Retrieve]', require('./lib/user_profile_retrieve'));
    // describe('[User Group Create]', require('./lib/user_group_create'));
});