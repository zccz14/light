const mongoose = require('mongoose');
const Schema = mongoose.Schema;

const SubmissionSchema = new Schema({
  submitterId: Schema.Types.ObjectId,
  judgerId: Schema.Types.ObjectId,
  problemId: Schema.Types.ObjectId,
  problemListId: Schema.Types.ObjectId,
  type: {type: String, default: 'plain'},
  body: Schema.Types.Buffer,
  sentence: String
});

module.exports = SubmissionSchema;