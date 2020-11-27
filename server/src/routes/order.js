const express = require('express');
const router = express.Router();
const { authJWT } = require("../middlewares");

const orderingController = require('../app/controllers/OrderingController');

router.post('/', [authJWT.verifyToken, authJWT.isCustomer], orderingController.saveOrder);
router.get('/id/:id', [authJWT.verifyToken, authJWT.isCustomer], orderingController.showByID);
router.get('/get', [authJWT.verifyToken, authJWT.isCustomer], orderingController.show);
router.post('/cancel', [authJWT.verifyToken, authJWT.isCustomer], orderingController.cancelOrder);

module.exports = router;