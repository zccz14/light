const mongoose = require('mongoose');
const Schema = mongoose.Schema;

var GroupRoleSchema = new Schema({
    userId: {
        type: Schema.Types.ObjectId,
        required: true,
    },
    name: {
        type: String,
        required: true,
    },
    role: {
        type: String,
        required: true,
        enum: ['owner', 'member'],
        default: 'member'
    }
});

var GroupSchema = new Schema({
    name: {
        type: String,
        required: true,
        unique: true
    },
    members: [GroupRoleSchema]
});


const Group = mongoose.model('group', GroupSchema);
module.exports = Group;