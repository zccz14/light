const mongoose = require('mongoose');
const Schema = mongoose.Schema;

const SystemSchema = new Schema({
    administrators: [Schema.Types.ObjectId]
});

module.exports = SystemSchema;