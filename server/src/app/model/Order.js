const mongoose = require('mongoose');

const Schema = mongoose.Schema;

const Ordering = new Schema({
    status: String,
    orderPosition: Object,
    foods: Array,
    user_id: { type: Schema.Types.ObjectId, ref: 'User' },
    restaurant_id: { type: Schema.Types.ObjectId, ref: 'Restaurant' },
    shipper_id: { type: Schema.Types.ObjectId, ref: 'User' },
}, {
    timestamps: true,
});

module.exports = mongoose.model('Ordering', Ordering);