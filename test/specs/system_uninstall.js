const co = require('co');
const should = require('chai').should();
const expect = require('chai').expect;

const testConfig = require('../config');

const SystemInstallHelper = require('../helpers/system_install');
const SystemUninstallHelper = require('../helpers/system_uninstall');

function SystemUninstall() {
  it('Normal Uninstall', function(done) {
    co(function * () {
      yield SystemInstallHelper(testConfig.theSA);
      let res = yield SystemUninstallHelper();
      res.body.code.should.equal(0);
      done();
    }).catch(done);
  });
  // it('Uninstall in Production Environment', function (done) {
  //     co(function* () {
  //         yield SystemInstallHelper(Object.assign({
  //             environment: 'production'
  //         }, testConfig.theSA));
  //         let res = yield SystemUninstallHelper();
  //         res.body.code.should.equal(7);
  //         yield require('mongoose').connection.db.dropDatabase();
  //         done();
  //     }).catch(done);
  // });
}

module.exports = SystemUninstall;