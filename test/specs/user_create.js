const co = require('co');
const should = require('chai').should();
const expect = require('chai').expect;

const UserCreateHelper = require('../helpers/user_create');

const tConfig = require('../config');

function UserCreateSpec() {
    it('Normal Create User', function (done) {
        co(function* () {
            let res = yield UserCreateHelper(tConfig.theUser);
            res.body.code.should.equal(0);
            done();
        }).catch(done);
    });
    it('Create Duplicated User', function(done) {
        co(function*(){
            yield UserCreateHelper(tConfig.theUser);
            let res = yield UserCreateHelper(tConfig.theUser);
            res.body.code.should.equal(3);
        }).catch(done);
    });
}

module.exports = UserCreateSpec;