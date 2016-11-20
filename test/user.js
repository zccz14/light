const request = require('supertest');
const app = require('../server');

describe('User API Testing', function () {
    it('retriving all the users and return json', function (done) {
        request(app)
            .get('/user')
            .set('Accept', 'application/json')
            .expect(200)
            .end(function (err, res) {
                if (err) return done(err);
                done();
            });
    });
});