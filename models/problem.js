const mongoose = require('mongoose');
const Schema = mongoose.Schema;

var ProblemSchema = new Schema({
    title: {
        type: String,
        required: true
    }, 
    description: {
        type: String,
        required: true
    }
});

const Problem = mongoose.model('problem', ProblemSchema);
module.exports = Problem;