const createError = require('http-errors');

const Restaurant = require("../model/Restaurant");
const FoodCategory = require("../model/FoodCategory");
const Food = require("../model/Food");
const { mongooseToObject } = require("../../utils/mongoose");
const { multipleMongooseToObject } = require("../../utils/mongoose");

class RestaurantController {
    //[GET] /restaurants/id/:id
    async showOneByID(req, res, next) {
        try {
            let rest = await Restaurant.findOne({ _id: req.params.id }).select('-createdAt -updatedAt -__v');
            if (rest) {
                rest = mongooseToObject(rest);
                let foodCategories = await FoodCategory.find({ restaurant_id: req.params.id }).select('name');
                foodCategories = multipleMongooseToObject(foodCategories);

                for (let i = 0; i < foodCategories.length; i++) {
                    const id_foodCategory = foodCategories[i]._id;

                    let foods = await Food.find({ category_id: id_foodCategory }).select('name decription price image_url');
                    foods = multipleMongooseToObject(foods);

                    foodCategories[i].foods = foods;
                }
                rest.menu = foodCategories;

                res.send(rest);
            } else {
                next(createError(404));
            }
        } catch (err) {
            next(createError(400, err));
        }
    }

    //[GET] /restaurants/:restaurant_url
    async showOneByURL(req, res, next) {
        try {
            let rest = await Restaurant.findOne({ restaurant_url: req.params.restaurant_url }).select('-createdAt -updatedAt -__v');
            if (rest) {
                rest = mongooseToObject(rest);
                let foodCategories = await FoodCategory.find({ restaurant_id: rest._id }).select('name');
                foodCategories = multipleMongooseToObject(foodCategories);

                for (let i = 0; i < foodCategories.length; i++) {
                    const id_foodCategory = foodCategories[i]._id;

                    let foods = await Food.find({ category_id: id_foodCategory }).select('name decription price image_url');
                    foods = multipleMongooseToObject(foods);

                    foodCategories[i].foods = foods;
                }

                rest.menu = foodCategories;

                res.send(rest);
            } else {
                next(createError(404));
            }
        } catch (err) {
            next(createError(400, err));
        }
    }

    //[GET] /restaurants?page=?&limit=?
    async showRestaurants(req, res, next) {
        try {
            let resPerPage = parseInt(req.query.limit); // results per page
            let page = parseInt(req.query.page); // page
            if (!page || !resPerPage) {
                page = 1;
                resPerPage = 10;
            }
            let rest = await Restaurant.find().select('name restaurant_url address phones image operating price_range city_id')
                .skip(resPerPage * page - resPerPage)
                .limit(resPerPage);

            if (rest.length) {
                rest = multipleMongooseToObject(rest);
                res.send(rest);
            } else {
                next(createError(404));
            }
        } catch (err) {
            next(createError(400, err));
        }
    }

    //[POST] /restaurants?ids[]
    async showRestaurantsByArrayID(req, res, next) {
        try {
            let ids = req.body.ids;
            //console.log(ids);
            let result = Array();
            for (let i = 0; i < ids.length; i++) {
                let rest = await Restaurant.find({ _id: ids[i] }, '-__v -createdAt -updatedAt');
                result = result.concat(rest);
            }

            if (result.length) {
                res.send(result);
            } else {
                next(createError(404));
            }
        } catch (err) {
            next(createError(400, err));
        }
    }
}

module.exports = new RestaurantController();