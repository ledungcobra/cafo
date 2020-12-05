const createError = require('http-errors');

const Food = require('../model/Food');
const FoodCategory = require('../model/FoodCategory');

const { mongooseToObject } = require('../../utils/mongoose')
const { multipleMongooseToObject } = require('../../utils/mongoose')

class MenuController {
    //[GET] /menus?res_id=?
    async show(req, res, next) {
        try {
            let res_id = req.query.res_id;

            let foodCategories = await FoodCategory.find({ restaurant_id: res_id }).select('name')
            if (foodCategories.length) {
                foodCategories = multipleMongooseToObject(foodCategories);

                for (let i = 0; i < foodCategories.length; i++) {
                    const id_foodCategory = foodCategories[i]._id;

                    let foods = await Food.find({ category_id: id_foodCategory }).select('name decription price image_url');
                    foods = multipleMongooseToObject(foods);

                    foodCategories[i].foods = foods;
                }

                res.send(foodCategories);
            } else {
                next(createError(404));
            }
        } catch (err) {
            next(createError(400, err));
        }
    }
}

module.exports = new MenuController();