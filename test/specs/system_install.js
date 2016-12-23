const co = require('co');
const should = require('chai').should();
const expect = require('chai').expect;

const testConfig = require('../config');

const SystemInstallHelper = require('../helpers/system_install');
const SystemUninstallHelper = require('../helpers/system_uninstall');

function SystemInstallSpec() {
  it('Normal Install', function(done) {
    co(function * () {
      let res = yield SystemInstallHelper(testConfig.theSA);
      res.body.code.should.equal(0);
      done();
    }).catch(done);
  });
  it('Empty req.body', function(done) {
    co(function * () {
      let res = yield SystemInstallHelper({});
      res.body.code.should.equal(2);
      done();
    }).catch(done);
  });
  it('Install After Installed', function(done) {
    co(function * () {
      let res;
      res = yield SystemInstallHelper(testConfig.theSA);
      res.body.code.should.equal(0);
      res = yield SystemInstallHelper(testConfig.theSA);
      res.body.code.should.equal(7);
      done();
    }).catch(done);
  });
  afterEach(() => SystemUninstallHelper());
}

module.exports = SystemInstallSpec;