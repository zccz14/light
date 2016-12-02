const co = require('co');
const should = require('chai').should();
const expect = require('chai').expect;
const request = require('supertest');
const app = require('../server');
const User = require('../models/user');

describe('User Sign In', function () {
    var aUser = {
        email: 'zccz14@function-x.org',
        password: 'world233'
    }
    var illegalEmail = 'zccz14';
    var nonExistEmail = 'zccz1444@function-x.org';
    var wrongPassword = 'world333';

    before('create a user before sign in', function (done) {
        co(function* () {
            var res1 = yield request(app)
                .post('/user')
                .set('Accept', 'application/json')
                .send(aUser)
                .expect(200);
            res1.body.code.should.equal(0);
            done();
        });
    });
    it('correct sign in', function (done) {
        co(function* () {
            var res1 = yield request(app)
                .post('/user/sign-in')
                .set('Accept', 'application/json')
                .send(aUser)
                .expect(200);
            res1.body.code.should.equal(0);
            done();
        });
    });
    it('a nonexist user', function (done) {
        co(function* () {
            var res1 = yield request(app)
                .post('/user/sign-in')
                .set('Accept', 'application/json')
                .send({ email: nonExistEmail, password: aUser.password })
                .expect(200);
            res1.body.code.should.equal(5);
            done();
        });
    });
    it('wrong password', function (done) {
        co(function* () {
            var res1 = yield request(app)
                .post('/user/sign-in')
                .set('Accept', 'application/json')
                .send({ email: aUser.email, password: wrongPassword })
                .expect(200);
            res1.body.code.should.equal(5);
            done();
        });
    });
    it('empty email', function (done) {
        co(function* () {
            var res1 = yield request(app)
                .post('/user/sign-in')
                .set('Accept', 'application/json')
                .send({ password: wrongPassword })
                .expect(200);
            res1.body.code.should.equal(5);
            done();
        });
        
    });
    it('empty password', function (done) {
        request(app)
            .post('/user/sign-in')
            .set('Accept', 'application/json')
            .send({ email: aUser.email })
            .expect(200)
            .end(function (err, res) {
                expect(err).to.be.null;
                res.body.code.should.equal(5);
                done();
            });
    });
    it('illegal email', function (done) {
        request(app)
            .post('/user/sign-in')
            .set('Accept', 'application/json')
            .send({ email: illegalEmail, password: aUser.password })
            .expect(200)
            .end(function (err, res) {
                expect(err).to.be.null;
                res.body.code.should.equal(5);
                done();
            });
    });
    after('drop users after tests', function (done) {
        User.remove({}, done);
    });
});