const mongoose = require('mongoose');

const Schema = mongoose.Schema;

const moment = require('moment-timezone');
const dateVietnam = moment.tz(Date.now(), 'Asia/Ho_Chi_Minh');

const Order = new Schema({
    status: String,
    orderPosition: Object,
    foods: Array,
    user_id: { type: Schema.Types.ObjectId, ref: 'User' },
    restaurant_id: { type: Schema.Types.ObjectId, ref: 'Restaurant' },
    shipper_id: { type: Schema.Types.ObjectId, ref: 'User' },
    order_time: { type: Date, default: dateVietnam }
}, {
    timestamps: true,
});

module.exports = mongoose.model('Order', Order);