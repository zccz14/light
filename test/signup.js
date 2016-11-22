const should = require('chai').should();
const expect = require('chai').expect;
const request = require('supertest');
const app = require('../server');
const db = require('../db');
const config = require('../config');

describe('User Sign Up', function () {
    // create index on users before all the tests
    before(function (done) {
        db.collection('users').createIndex({ email: 1 }, { unique: true }, done);
    });

    var aUser = {
        email: 'hello@function-x.org',
        password: 'world233'
    };
    var mustLegalEmail = 'zccz14@function-x.org';
    var mustLegalPassword =
        '1'.repeat(config.user.password.minimumLength) +
        'a'.repeat(config.user.password.minimumLowercaseLetter) +
        '1'.repeat(config.user.password.minimumNumeral);

    it('create a user', function (done) {
        request(app)
            .post('/user')
            .set('Accept', 'application/json')
            .send(aUser)
            .expect(200)
            .end((err, res) => {
                expect(err).to.be.null;
                res.body.code.should.equal(0);
                done();
            });
    });
    it('create an email-duplicated user', function (done) {
        request(app)
            .post('/user')
            .set('Accept', 'application/json')
            .send(aUser)
            .expect(200)
            .end((err, res) => {
                expect(err).to.be.null;
                res.body.code.should.be.equal(11);
                done();
            });
    });
    it('create a user whose email is illegal', function (done) {
        request(app)
            .post('/user')
            .set('Accept', 'application/json')
            .send({
                email: 'hello',
                password: 'world233'
            })
            .expect(200)
            .end((err, res) => {
                expect(err).to.be.null;
                res.body.code.should.be.equal(2);
                done();
            });
    });
    it('create a user whose password is empty', function (done) {
        request(app)
            .post('/user')
            .set('Accept', 'application/json')
            .send({ email: mustLegalEmail })
            .expect(200)
            .end((err, res) => {
                expect(err).to.be.null;
                res.body.code.should.be.equal(13);
                res.body.body.should.be.equal('password');
                done();
            });
    });
    it('create a user whose email is empty', function (done) {
        request(app)
            .post('/user')
            .set('Accept', 'application/json')
            .send({ password: 'zc123213' })
            .expect(200)
            .end((err, res) => {
                expect(err).to.be.null;
                res.body.code.should.be.equal(13);
                res.body.body.should.be.equal('email');
                done();
            });
    });
    it('create a user whose password is too short', function (done) {
        request(app)
            .post('/user')
            .set('Accept', 'application/json')
            .send({
                email: mustLegalEmail,
                password: 'a'.repeat(config.user.password.minimumLength - 1)
            })
            .expect(200)
            .end((err, res) => {
                expect(err).to.be.null;
                res.body.code.should.be.equal(3);
                done();
            });
    });
    it('create a user whose password has not enough lowercase letter', function (done) {
        request(app)
            .post('/user')
            .set('Accept', 'application/json')
            .send({
                email: mustLegalEmail,
                password: '1'.repeat(config.user.password.minimumLength) + 'a'.repeat(config.user.password.minimumLowercaseLetter - 1)
            })
            .expect(200)
            .end((err, res) => {
                expect(err).to.be.null;
                res.body.code.should.be.equal(5);
                done();
            });
    });
    it('create a user whose password has not enough numberals', function (done) {
        request(app)
            .post('/user')
            .set('Accept', 'application/json')
            .send({
                email: mustLegalEmail,
                password: 'a'.repeat(config.user.password.minimumLength) + '1'.repeat(config.user.password.minimumNumeral - 1)
            })
            .expect(200)
            .end((err, res) => {
                expect(err).to.be.null;
                res.body.code.should.be.equal(7);
                done();
            });
    });
    it('create another legal user', function (done) {
        request(app)
            .post('/user')
            .set('Accept', 'application/json')
            .send({
                email: mustLegalEmail,
                password: mustLegalPassword
            })
            .expect(200)
            .end((err, res) => {
                expect(err).to.be.null;
                res.body.code.should.be.equal(0);
                done();
            });
    });
    // drop users after all the test
    after(function (done) {
        db.collection('users').drop(done);
    });
});