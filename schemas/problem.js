const mongoose = require('mongoose');
const Schema = mongoose.Schema;

var ProblemSchema = new Schema({
  ownerId: Schema.Types.ObjectId,
  title: {type: String, required: true},
  description: {type: String, required: true}
});

module.exports = ProblemSchema;