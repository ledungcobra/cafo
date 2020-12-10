const mongoose = require('mongoose');

const Schema = mongoose.Schema;
const Role = require('./Role');

const User = new Schema({
    username: String,
    email: String,
    password: String,
    phone_number: String,
    roles: [{
        type: mongoose.Schema.Types.ObjectId,
        ref: "Role"
    }]
}, {
    timestamps: true,
});

module.exports = mongoose.model('User', User);