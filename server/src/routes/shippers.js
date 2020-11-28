var express = require('express')
var router = express.Router()

const { authJWT } = require("../middlewares");

const shipperController = require('../app/controllers/ShipperController');

router.get('/search', [authJWT.verifyToken, authJWT.isShipper], shipperController.searchOrdering);
router.get('/search-orders', [authJWT.verifyToken, authJWT.isShipper], shipperController.searchOrdering);
router.post('/get-order', [authJWT.verifyToken, authJWT.isShipper], shipperController.getOrder);
router.post('/cancel-order', [authJWT.verifyToken, authJWT.isShipper], shipperController.cancelOrder);

module.exports = router;