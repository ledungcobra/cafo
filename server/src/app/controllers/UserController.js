const User = require('../model/User')
const { mongooseToObject } = require('../../utils/mongoose');

class UserController {
    getInfo = async(req, res, next) => {
        const user = await User.find({ _id: req.userID }, '-__v').populate("roles", "-__v");
        user = mongooseToObject(user);
        res.send(user);
    }
}

module.exports = new UserController();