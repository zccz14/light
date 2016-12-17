const mongoose = require('mongoose');
const Schema = mongoose.Schema;

var ProblemListSchema = new Schema({
  ownerId: {
    type: Schema.Types.ObjectId,
    required: true,
    unique: true
  },
  name: {
    type: String,
    required: true,
    unique: true
  },
  problems: [Schema.Types.ObjectId],
  public: {
    type: Boolean,
    default: false
  }
});

const ProblemList = mongoose.model('problemList', ProblemListSchema);
module.exports = ProblemList;