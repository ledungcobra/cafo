const mongoose = require('mongoose')
const Schema = mongoose.Schema;

const City = new Schema({
    name: { type: String },
    city_url: { type: String },
}, {
    timestamps: true,
});

module.exports = mongoose.model('City', City);