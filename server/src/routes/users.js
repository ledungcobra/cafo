var express = require('express')
var router = express.Router()

const { authJWT } = require("../middlewares");

const userController = require('../app/controllers/UserController');

router.get('/', [authJWT.verifyToken], userController.getInfo);

module.exports = router;