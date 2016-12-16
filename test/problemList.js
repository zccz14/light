const co = require('co');
const should = require('chai').should();
const expect = require('chai').expect;
const request = require('supertest');
const app = require('../server');
const User = require('../models/user');
const Group = require('../models/group');

describe('update problemlist', function () {
  var aUser = {
    email: 'zccz14@function-x.org',
    password: 'world233',
    username: 'zccz14'
  }
  var illegalEmail = 'zccz14';
  var nonExistEmail = 'zccz1444@function-x.org';
  var wrongPassword = 'world333';
  var thisListName = "xjtuse";
  var newListName = 'xjtuse42';
  var problemListName = "BUZUOBUSHIZHONGGUOREN";
  var cookie;
  before('create a user and a list before update', function (done) {
    co(function* () {
      var res1 = yield request(app)
        .post('/user')
        .set('Accept', 'application/json')
        .send(aUser)
        .expect(200);
      res1.body.code.should.equal(0);
      cookie = res1.headers['set-cookie'];
      var res2 = yield request(app)
        .post('/user/sign-in')
        .set('Accept', 'application/json')
        .set('Cookie', cookie)
        .send(aUser)
        .expect(200);
      res2.body.code.should.equal(0);
      var res3 = yield request(app)
        .post('/problemlist')
        .set('Accept', 'application/json')
        .set('Cookie', cookie)
        .send({ listName: thisListName })
        .expect(200);
      res3.body.code.should.equal(0);
      done();
    }).catch(done);
  });



  it('add a problem to the list', function (done) {
    co(function* () {
      var res1 = yield request(app)
        .put(`/problem_list/${problemListName}`)
        .set('Accept', 'application/json')
        .set('Cookie', cookie)
        .send({ listName: newListName })
        .expect(200);
      res1.body.code.should.equal(0);
      done();
    }).catch(done);
  });

  it('get the problemlist', function (done) {
    co(function* () {
      var res1 = yield request(app)
        .get(`/problem_list/${problemListName}`)
        .set('Accept', 'application/json')
        .set('Cookie', cookie)
        .expect(200);
      res1.body.code.should.equal(0);
      done();
    }).catch(done);
  });


  after('drop users after tests', function (done) {
    User.remove({}, done);
    ProblemList.remove({}, done);
  });
});