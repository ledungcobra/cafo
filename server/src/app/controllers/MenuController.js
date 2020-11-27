const Food = require('../model/Food');
const FoodCategory = require('../model/FoodCategory');

const { mongooseToObject } = require('../../utils/mongoose')
const { multipleMongooseToObject } = require('../../utils/mongoose')

class MenuController {
    //[GET] /menus?res_id=?
    async show(req, res, next) {
        let res_id = req.query.res_id;

        let foodCategories = await FoodCategory.find({ restaurant_id: res_id }).select('name')

        foodCategories = multipleMongooseToObject(foodCategories);

        for (let i = 0; i < foodCategories.length; i++) {
            const id_foodCategory = foodCategories[i]._id;

            let foods = await Food.find({ category_id: id_foodCategory }).select('name decription price image_url');
            foods = multipleMongooseToObject(foods);

            foodCategories[i].foods = foods;
        }

        res.send(foodCategories);
    }
}

module.exports = new MenuController();