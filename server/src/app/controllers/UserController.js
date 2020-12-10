const createError = require('http-errors');

const User = require('../model/User')
const { mongooseToObject } = require('../../utils/mongoose');
class UserController {

    //[GET] /users
    getInfo = async(req, res, next) => {
        try {
            let user = await User.findOne({ _id: req.userID }, '-__v -password -createdAt -updatedAt').populate("roles", "-__v");
            if (user) {
                user = mongooseToObject(user);
                res.send(user);
            } else {
                next(createError(404));
            }
        } catch (error) {
            next(createError(400, 'User was not found!'))
        }
    }
}

module.exports = new UserController();