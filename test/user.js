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
                if (res.body.code != 0) throw new Error('return non-zero value');
                if (res.body.body.username != newUser.username) throw new Error('明明是我先来的');
                newUser._id = res.body.body._id;
            })
            .end(function(err, res) {
                if (err) return done(err);
                _id = res.body.body._id;
                done();
            });
    });
    it('update a exist user (change password)', function (done) {
        var newPass = '23333333';
        request(app)
            .put(`/user/${newUser._id}`)
            .set('Accept', 'application/json')
            .send({password: newPass})
            .expect(200)
            .expect((res) => {
                console.log(res.body);
                if (res.body.code != 0) throw new Error('return non-zero value');
                if (res.body.body.value.password != newUser.password) throw new Error('not origin password');
            })
            .end(function(err, res) {
                if (err) return done(err);
                newUser.password = newPass;
                done();
            });
    });
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