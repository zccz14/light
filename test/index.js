var request = require('supertest');
var app = require('../server');

describe('Index', function () {
  it('respond index page', function (done) {
    request(app)
      .get('/')
      .expect(200, done);
  });
});