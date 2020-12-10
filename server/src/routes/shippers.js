var express = require('express')
var router = express.Router()

const { authJWT } = require("../middlewares");

const shipperController = require('../app/controllers/ShipperController');

router.get('/orders', [authJWT.verifyToken, authJWT.isShipper], shipperController.show);
router.get('/search', [authJWT.verifyToken, authJWT.isShipper], shipperController.searchOrdering);

//v2 fix amount => count
router.get('/search-v2', [authJWT.verifyToken, authJWT.isShipper], shipperController.searchOrderingV2);

//v2
router.get('/search-orders', [authJWT.verifyToken, authJWT.isShipper], shipperController.searchOrdering);

router.post('/get-order', [authJWT.verifyToken, authJWT.isShipper], shipperController.getOrder);
router.post('/cancel-order', [authJWT.verifyToken, authJWT.isShipper], shipperController.cancelOrder);
router.post('/finish-order', [authJWT.verifyToken, authJWT.isShipper], shipperController.finishOrder);

module.exports = router;