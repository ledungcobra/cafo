//const restaurants = require('../resources/raw-data/data.json');

const { mongooseToObject } = require('../utils/mongoose')
const { multipleMongooseToObject } = require('../utils/mongoose')

const City = require('../app/model/City');
const Food = require('../app/model/Food');
const Restaurant = require('../app/model/Restaurant');
const FoodCategory = require('../app/model/FoodCategory');

saveData = async(restaurants) => {
    for (let index = 0; index < restaurants.length; index++) {
        let city = await City.findOne({ city_url: restaurants[index].location_url });
        city = mongooseToObject(city);
        if (!city) {
            return;
        }

        let operating = new Object();
        operating.close_time = restaurants[index].operating.close_time;
        operating.open_time = restaurants[index].operating.open_time;

        let restaurant = new Restaurant({
            name: restaurants[index].name,
            restaurant_url: restaurants[index].restaurant_url,
            address: restaurants[index].address,
            phones: restaurants[index].phones,
            image: restaurants[index].photos[restaurants[index].photos.length - 1],
            operating: operating,
            position: restaurants[index].position,
            price_range: restaurants[index].price_range,

            city_id: city
        });

        restaurant.save(function(err) {
            if (err) return handleError(err);
        });

        const menus = restaurants[index].menu_infos;

        for (let menuIndex = 0; menuIndex < menus.length; menuIndex++) {
            let foodCategory = new FoodCategory({
                name: menus[menuIndex].dish_type_name,
                restaurant_id: restaurant._id,
            });

            foodCategory.save(function(err) {
                if (err) return handleError(err);
            });

            const foods = menus[menuIndex].dishes;

            for (let foodIndex = 0; foodIndex < foods.length; foodIndex++) {
                let food = new Food({
                    name: foods[foodIndex].name,
                    decription: foods[foodIndex].description,
                    price: foods[foodIndex].price,
                    image_url: foods[foodIndex].photos[foods[foodIndex].photos.length - 1],

                    category_id: foodCategory._id,
                })

                food.save(function(err) {
                    if (err) return handleError(err);
                });
            }
        }

    }

    console.log('Success save data!');
}

module.exports = saveData;