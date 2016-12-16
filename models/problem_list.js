const mongoose = require('mongoose');
const Schema = mongoose.Schema;

var ProblemSchema = new Schema({
  problemName: {
    type: String,
    required: true,
    unique: true
  }
});

var ProblemListSchema = new Schema({
  ListName: {
    type: String,
    required: true,
    unique: true
  },
  problems: [ProblemSchema]
});

const ProblemList = mongoose.model('problemList', ProblemListSchema);
module.exports = ProblemList;