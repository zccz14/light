const mongoose = require('mongoose');
const Schema = mongoose.Schema;

var ProblemListSchema = new Schema({
    ownerId: {
        type: Schema.Types.ObjectId,
        required: true
    },
    name: {
        type: String,
        required: true
    },
    problems: [Schema.Types.ObjectId],
    public: {
        type: Boolean,
        default: false
    }
});

module.exports = ProblemListSchema;