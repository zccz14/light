const request = require('supertest');
const app = require('../server');
const expect = require('expect.js');

describe('User API Testing', function () {
    it('retrieve return json', function (done) {
        request(app)
            .get('/user')
            .set('Accept', 'application/json')
            .expect(200)
            .end(function (err, res) {
                if (err) return done(err);
                done();
            });
    });
    var theUser = {
        username: 'zccz14',
        password: '23336666'
    };
    it('create first user', function (done) {
        request(app)
            .post('/user')
            .set('Accept', 'application/json')
            .send({ username: theUser.username, password: theUser.password })
            .expect(200)
            .end(function (err, res) {
                if (err) return done(err);
                expect(res.body.code).to.equal(0);
                theUser._id = res.body.body._id;
                expect(res.body.body).to.eql(theUser);         
                done();
            });
    });
    it('update a exist user (change password)', function (done) {
        var newPass = '23333333';
        request(app)
            .put(`/user/${theUser._id}`)
            .set('Accept', 'application/json')
            .send({ password: newPass })
            .expect(200)
            .end(function (err, res) {
                if (err) return done(err);
                expect(res.body.code).to.be(0);
                theUser.password = newPass;
                done();
            });
    });
    it('update a non-exist user (invalid userid)', function (done) {
        request(app)
            .put(`/user/heiheihei`)
            .set('Accept', 'application/json')
            .send({ username: 'haha' })
            .expect(200)
            .expect((res) => {
                if (res.body.code != 1) throw new Error('[wrong return code]: invalid ObjectID');
            })
            .end((err, res) => {
                if (err) return done(err);
                done();
            });
    });
});