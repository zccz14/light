const mongoose = require('mongoose');
const Schema = mongoose.Schema;

const SystemSchema = new Schema({
    administrators: [Schema.Types.ObjectId]
});

const System = mongoose.model('system', SystemSchema);
module.exports = System;