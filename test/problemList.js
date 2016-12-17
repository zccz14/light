const co = require('co');
const should = require('chai').should();
const expect = require('chai').expect;
const request = require('supertest');
const app = require('../server');
const User = require('../models/user');
const ProblemList = require('../models/problem_list');

describe('update problemlist', function () {
  var aUser = {
    email: 'zccz14@function-x.org',
    password: 'world233',
    username: 'zccz14'
  }
  var illegalEmail = 'zccz14';
  var nonExistEmail = 'zccz1444@function-x.org';
  var wrongPassword = 'world333';
  var thisListName = "BUZUOBUSHIZHONGGUOREN";
  var newListName = 'goodList';
  var problemListId;
  var cookie;
  var thisUser;
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
      thisUser = req.session.user;
      res2.body.code.should.equal(0);
      done();
    }).catch(done);
  });



  it('add a problem to the list', function (done) {
    co(function* () {
      var res1 = yield request(app)
        .put(`/problem_list/${problemListName}`)
        .set('Accept', 'application/json')
        .set('Cookie', cookie)
        .send({
          listName: newListName
        })
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
    co(function* () {
      yield User.remove({}).exec();
      yield ProblemList.remove({}).exec();
      done();
    })
  });
});