const mongoose = require('mongoose');
const Schema = mongoose.Schema;

var ProblemListSchema = new Schema({
  ownerId: {type: Schema.Types.ObjectId, required: true},
  name: {type: String, required: true},
  problems: [Schema.Types.ObjectId],
  visibility:
      {type: String, enum: ['public', 'protect', 'private'], default: 'public'},
  allowSubmitting: {type: Boolean, default: true}
});

module.exports = ProblemListSchema;