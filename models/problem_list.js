const mongoose = require('mongoose');
const
const Schema = mongoose.Schema;


var ProblemListSchema = new Schema({
  ownerId: {
    type: Schema.Types.ObjectId,
    required: true,
    unique: true
  },
  listName: {
    type: String,
    required: true,
    unique: true
  },
  problems: [ProblemSchema]
});

const ProblemList = mongoose.model('problemList', ProblemListSchema);
module.exports = ProblemList;