const mongoose = require('mongoose')
const Schema = mongoose.Schema;
const Restaurant = require('./Restaurant')

const FoodCategory = new Schema({
    name: { type: String },
    restaurant_id: { type: Schema.Types.ObjectId, ref: 'Restaurant' },
}, {
    timestamps: true,
});

module.exports = mongoose.model('FoodCategory', FoodCategory);