const mongoose = require("mongoose");
const Schema = mongoose.Schema;

const FoodCategory = require("./FoodCategory");
const Restaurant = require("./Restaurant");

const Food = new Schema({
    name: { type: String },
    decription: { type: String },
    price: { type: Object },
    image_url: { type: Object },

    category_id: { type: Schema.Types.ObjectId, ref: "FoodCategory" },
}, {
    timestamps: true,
});

module.exports = mongoose.model("Food", Food);

/*
cafo_dev.foods
+ id
+ description
+ name
+ price
+ photos[]
*/