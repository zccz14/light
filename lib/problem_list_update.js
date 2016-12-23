const co = require('co');
const mongoose = require('mongoose');
const ObjectId = mongoose.Types.ObjectId;

const ProblemList = require('../models/problem_list');
const User = require('../models/user');

const OnError = require('./on_error');

function ProblemListUpdate(req, res, next) {
  co(function * () {
    let user = req.session.user;
    let thisProblemTitle = req.body.problemTitle;
    let thisProblemDescription = req.body.problemDescription;
    let thisProblemListId = req.params._id;
    var isOwner = false;
    let thisProblemList =
        yield ProblemList.findById(new ObjectId(thisProblemListId)).exec();
    if (thisProblemList == null) {
      res.json({code: 11});
      return;
    }
    if (user._id.toString() === thisProblemList.ownerId.toString()) {
      isOwner = true;
    }
    let judgeGroup = yield User.findOne({
      '_id': new ObjectId(user._id),
      'roles.$.role': 'owner',
      'roles.$.group': thisProblemList._id
    });
    if (judgeGroup) {
      isOwner = true;
    }
    if (isOwner) {
      problemList =
          yield ProblemList.update({"_id": new Object(thisProblemListId)}, {
            $push: {
              Problem: [
                {
                  title: thisProblemTitle,
                  description: thisProblemDescription
                  // insert promlem content here
                }
              ]
            }
          });
      res.json({code: 0, problemList});
    } else {
      res.json({code: 7, msg: "Authentication denied"})
    }

  }).catch(OnError(req, res));
}

module.exports = ProblemListUpdate;