const mongoose = require('mongoose');
const Schema = mongoose.Schema;

const SubmissionSchema = new Schema({
  submitterId: Schema.Types.ObjectId,
  judgerId: Schema.Types.ObjectId,
  problemId: Schema.Types.ObjectId,
  problemListId: Schema.Types.ObjectId,
  body: Schema.Types.Buffer
});

module.exports = SubmissionSchema;