const co = require('co');

const testConfig = require('./config');

describe('[OrangeJuice]', function() {
  describe('[System Install]', require('./specs/system_install'));
  describe('[System Uninstall]', require('./specs/system_uninstall'));
  describe('[Development Environment]', function() {
    beforeEach(() => require('./helpers/system_install')(testConfig.theSA));
    afterEach(() => require('./helpers/system_uninstall')());
    describe('[User Create]', require('./specs/user_create'));
    describe('[User Login]', require('./specs/user_login'));
    describe('[User Logout]', require('./specs/user_logout'));
  });
  // describe('[User Login]', testUserLogin);
  // describe('[User Logout]', require('./lib/user_logout'));
  // describe('[User Profile Retrieve]',
  // require('./lib/user_profile_retrieve'));
  // describe('[User Group Create]', require('./lib/user_group_create'));
});