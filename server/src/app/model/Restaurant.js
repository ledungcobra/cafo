const mongoose = require('mongoose')
const Schema = mongoose.Schema;

const City = require('./City')

const Restaurant = new Schema({
    name: { type: String },
    restaurant_url: { type: String },
    address: { type: String },
    phones: { type: Array },
    image: { type: Object },
    operating: { type: Object },
    position: { type: Object },
    price_range: { type: Object },

    city_id: { type: Schema.Types.ObjectId, ref: 'City' },
}, {
    timestamps: true,
});

module.exports = mongoose.model('Restaurant', Restaurant);