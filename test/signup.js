const co = require('co');
const should = require('chai').should();
const expect = require('chai').expect;
const request = require('supertest');
const app = require('../server');
const config = require('../config');
const User = require('../models/user');

describe('User Sign Up', function () {
    var aUser = {
        email: 'hello@function-x.org',
        password: 'world233'
    };
    var mustLegalEmail = 'zccz14@function-x.org';
    var mustIllegalEmail = 'hello';
    var mustLegalPassword =
        '1'.repeat(config.user.password.minimumLength) +
        'a'.repeat(config.user.password.minimumLowercaseLetter) +
        '1'.repeat(config.user.password.minimumNumeral);
    var lengthIllegelPassword =
        '1' + 'a'.repeat(config.user.password.minimumLength - 2);
    var noEnoughLowercaseLetterPassword =
        '1'.repeat(config.user.password.minimumLength) +
        'a'.repeat(config.user.password.minimumLowercaseLetter - 1)
    var noEnoughNumeralPassword =
        'a'.repeat(config.user.password.minimumLength) +
        '1'.repeat(config.user.password.minimumNumeral - 1)

    it('create a user', function (done) {
        co(function* () {
            var res = yield request(app)
                .post('/user')
                .set('Accept', 'application/json')
                .send(aUser)
                .expect(200)
            res.body.code.should.equal(0);
            done();
        });
    });
    it('create an email-duplicated user', function (done) {
        co(function* () {
            var res = yield request(app)
                .post('/user')
                .set('Accept', 'application/json')
                .send(aUser)
                .expect(200)
            res.body.code.should.equal(3);
            done();
        });
    });
    it('create a user whose email is illegal', function (done) {
        co(function* () {
            var res = yield request(app)
                .post('/user')
                .set('Accept', 'application/json')
                .send({
                    email: mustIllegalEmail,
                    password: mustLegalPassword
                })
                .expect(200)
            res.body.code.should.equal(2);
            done();
        });
    });
    it('create a user whose password is empty', function (done) {
        co(function* () {
            var res = yield request(app)
                .post('/user')
                .set('Accept', 'application/json')
                .send({ email: mustLegalEmail })
                .expect(200)
            res.body.code.should.equal(2);
            done();
        });
    });
    it('create a user whose email is empty', function (done) {
        co(function* () {
            var res = yield request(app)
                .post('/user')
                .set('Accept', 'application/json')
                .send({ password: mustLegalPassword })
                .expect(200)
            res.body.code.should.equal(2);
            done();
        });
    });
    it('create a user whose password is too short', function (done) {
        co(function* () {
            var res = yield request(app)
                .post('/user')
                .set('Accept', 'application/json')
                .send({
                    email: mustLegalEmail,
                    password: lengthIllegelPassword
                })
                .expect(200)
            res.body.code.should.equal(2);
            done();
        });
    });
    it('create a user whose password has not enough lowercase letter', function (done) {
        co(function* () {
            var res = yield request(app)
                .post('/user')
                .set('Accept', 'application/json')
                .send({
                    email: mustLegalEmail,
                    password: noEnoughLowercaseLetterPassword
                })
                .expect(200)
            res.body.code.should.equal(2);
            done();
        });
    });
    it('create a user whose password has not enough numerals', function (done) {
        co(function* () {
            var res = yield request(app)
                .post('/user')
                .set('Accept', 'application/json')
                .send({
                    email: mustLegalEmail,
                    password: noEnoughNumeralPassword
                })
                .expect(200)
            res.body.code.should.equal(2);
            done();
        });
    });
    it('create another legal user', function (done) {
        co(function* () {
            var res = yield request(app)
                .post('/user')
                .set('Accept', 'application/json')
                .send({
                    email: mustLegalEmail,
                    password: mustLegalPassword
                })
                .expect(200)
            res.body.code.should.equal(0);
            done();
        });
    });
    after('drop all users after tests', function (done) {
        User.remove({}, done);
    });
});