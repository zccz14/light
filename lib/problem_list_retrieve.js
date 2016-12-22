const co = require('co');
const mongoose = require('mongoose');
const ObjectId = mongoose.Types.ObjectId;

const ProblemList = require('../models/problem_list');

const OnError = require('./on_error');

function ProblemListRetrieve(req, res, next) {
  co(function * () {
    // all public problem lists with query
    let $query = Object.assign({$or: [{public: true}]}, req.query);
    // if user has signed in
    if (req.session.user) {
      let user = req.session.user;
      // find problem lists of user's groups
      let ownerIds = user.roles.map(v => new ObjectId(v.group));
      // find user's problem lists
      ownerIds.push(new ObjectId(user._id));
      $query.$or.push({ownerId: {$in: ownerIds}});
    }
    let problemLists = yield ProblemList.find($query).exec();
    res.json({code: 0, problemLists});
  }).catch(OnError(req, res));
}

module.exports = ProblemListRetrieve;