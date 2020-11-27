const User = require('../model/User')
const Role = require('../model/Role');
const { model } = require('../model/Role');

class UserController {
    getInfo = async(req, res, next) => {
        const user = await User.find({ _id: req.userId }, '-__v').populate("roles", "-__v");
        res.send(user);
    }
}

module.exports = new UserController();