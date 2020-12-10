const express = require('express');
const router = express.Router();
const { authJWT } = require("../middlewares");

const orderController = require('../app/controllers/OrderController');

router.get('/id/:id', [authJWT.verifyToken, authJWT.isCustomer], orderController.showByID);
router.post('/cancel', [authJWT.verifyToken, authJWT.isCustomer], orderController.cancelOrder);
router.get('/', [authJWT.verifyToken, authJWT.isCustomer], orderController.show);
router.post('/', [authJWT.verifyToken, authJWT.isCustomer], orderController.saveOrder);

module.exports = router;