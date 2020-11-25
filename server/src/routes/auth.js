var express = require('express');
var router = express.Router();

const { verifySignUp } = require("../middlewares");
const authController = require("../app/controllers/AuthController");

router.post("/signup", [
        verifySignUp.checkDuplicateUsernameOrEmail,
        verifySignUp.checkRolesExisted
    ],
    authController.signup
);
router.post("/signin", authController.signin);

module.exports = router;