const mongoose = require('mongoose');
const Schema = mongoose.Schema;

const AdministratorSchema = new Schema({
    userId: {
        type: Schema.Types.ObjectId,
        required: true,
        unique: true
    }
});

const Administrator = mongoose.model('administrator', AdministratorSchema);
module.exports = Administrator;