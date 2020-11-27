var express = require('express')
var router = express.Router()

const restaurantController = require('../app/controllers/RestaurantController');

router.get('/:restaurant_url', restaurantController.showOneByURL);
router.get('/id/:id', restaurantController.showOneByID);
router.get('/', restaurantController.showRestaurants);
router.post('/', restaurantController.showRestaurantsByArrayID);

module.exports = router;