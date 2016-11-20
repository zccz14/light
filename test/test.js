var request = require('supertest');
var app = require('../server');

describe('GET /', function () {
  it('respond index page', function (done) {
    request(app)
      .get('/')
      .expect(200, done);
  });
});
