const mongoose = require('mongoose');
const Schema = mongoose.Schema;

const SystemSchema = new Schema({
  administrators: [Schema.Types.ObjectId],
  environment:
      {type: String, enum: ["production", "development"], required: true}
});

module.exports = SystemSchema;