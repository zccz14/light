const request = require('supertest');
const app = require('../server');

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
    var newUser = {
        username: 'zccz14',
        password: '23336666'
    };
    it('create first user', function (done) {
        request(app)
            .post('/user')
            .set('Accept', 'application/json')
            .send({username: newUser.username, password: newUser.password})
            .expect(200)
            .expect((res) => {
                if (res.code != 0) throw new Error('非零的返回');
                if (res.body.body.length > 1) throw new Error('怎么还有人');
                if (res.body.body[0].name != newUser.username) throw new Error('明明是我先来的');
                newUser._id = res.body.body[0]._id;
            })
            .end(function(err, res) {
                if (err) return done(err);
                _id = res.body.
                done();
            });
    });
    // it('update a exist user (change password)', function (done) {
    //     request(app)
    //         .put(`/user/${newUser._id}`)
    //         .set('Accept', 'application/json')
    //         .send({password: '23333333'})
    //         .expect(200)
    //         .expect({code: 0})
    //         .end(function(err, res) {
    //             if (err) return done(err);
    //             done();
    //         });
    // });
    // it('update a non-exist user', function (done) {
    //     request(app)
    //         .put(`/user/heiheihei`)
    //         .set('Accept', 'application/json')
    //         .send({username: 'haha'})
    //         .expect(200)
    //         .expect({code: 1})
    //         .end((err, res) => {
    //             if (err) return done(err);
    //             done();
    //         });
    // });
});