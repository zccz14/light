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

module.exports = ProblemSchema;