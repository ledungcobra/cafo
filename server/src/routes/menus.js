var express = require('express');

var router = express.Router()

const menuController = require('../app/controllers/MenuController');

router.get('/', menuController.show);

module.exports = router;