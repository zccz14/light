const should = require('chai').should();
const expect = require('chai').expect;
const request = require('supertest');
const app = require('../server');
const config = require('../config');
const User = require('../models/user');

describe('User Sign Out', function () {
    var aUser = {
        email: 'zccz14@function-x.org',
        password: 'world233'
    }
    var cookie;
    var noCookie = '';

    before('create a user', function (done) {
        request(app)
            .post('/user')
            .set('Accept', 'application/json')
            .send(aUser)
            .expect(200)
            .end(function (err, res) {
                expect(err).to.be.null;
                res.body.code.should.equal(0);
                cookie = res.headers['set-cookie'];
                done();
            });
    });

    it('correct sign-out', function (done) {
        //sign in first
        request(app)
            .post('/user/sign-in')
            .set('Accept', 'application/json')
            .set('Cookie', cookie)
            .send(aUser)
            .expect(200)
            .end(function (err, res) {
                expect(err).to.be.null;
                res.body.code.should.equal(0);
                //test sign-out
                request(app)
                    .get('/user/sign-out')
                    .set('Accept', 'application/json')
                    .expect(200)
                    .end(function (err, res) {
                        expect(err).to.be.null;
                        res.body.code.should.equal(0);
                        done();
                    });
            });
    });
    it('just signed up but have not signed in yet', function (done) {
        request(app)
            .get('/user/sign-out')
            .set('Accept', 'application/json')
            .set('Cookie', cookie)
            .expect(200)
            .end(function (err, res) {
                expect(err).to.be.null;
                res.body.code.should.equal(0);
                done();
            });
    });
    it('have not signed in or signed up yet', function (done) {
        request(app)
            .get('/user/sign-out')
            .set('Accept', 'application/json')
            .set('Cookie', noCookie)
            .expect(200)
            .end(function (err, res) {
                expect(err).to.be.null;
                res.body.code.should.equal(0);
                done();
            });
    });
    after(function (done) {
        User.remove({}, done);
    });
});
