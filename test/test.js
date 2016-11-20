var request = require('supertest');
var app = require('../server');

request(app)
  .get('/')
  .expect(200)
  .end(function (err, res) {
    if (err) throw err;
  });