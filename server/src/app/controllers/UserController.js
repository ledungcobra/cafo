const User = require('../model/User')
const { mongooseToObject } = require('../../utils/mongoose');
const { Mongoose } = require('mongoose');

class UserController {
    getInfo = async(req, res, next) => {
        let user = await User.findOne({ _id: req.userID }, '-__v -password').populate("roles", "-__v");
        user = mongooseToObject(user);
        res.send(user);
    }
}

module.exports = new UserController();