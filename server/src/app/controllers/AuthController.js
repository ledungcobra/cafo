const config = require('../../config/auth/auth');
const jwt = require("jsonwebtoken");
const bcrypt = require("bcryptjs");

const Role = require('../model/Role');
const User = require('../model/User');

const getMessageForClient = require('../../utils/message');

class AuthController {
    signupOld = (req, res) => {
        const user = new User({
            username: req.body.username,
            email: req.body.email,
            phone_number: req.body.phone_number,
            password: bcrypt.hashSync(req.body.password, 8)
        });
        user.save((err, user) => {
            if (err) {
                res.status(500).send(getMessageForClient(err));
                return;
            }
            if (req.body.roles) {
                Role.find({
                        name: { $in: req.body.roles }
                    },
                    (err, roles) => {
                        if (err) {
                            res.status(500).send(getMessageForClient(err));
                            return;
                        }
                        user.roles = roles.map(role => role._id);
                        user.save(err => {
                            if (err) {
                                res.status(500).send(getMessageForClient(err));
                                return;
                            }
                            res.send(getMessageForClient(res.statusCode, 'User was registered successfully!'));
                        });
                    }
                );
            } else {
                Role.findOne({ name: "user" }, (err, role) => {
                    if (err) {
                        res.status(500).send(getMessageForClient(err));
                        return;
                    }
                    user.roles = [role._id];
                    user.save(err => {
                        if (err) {
                            res.status(500).send(getMessageForClient(err));
                            return;
                        }
                        res.send(getMessageForClient(res.statusCode, 'User was registered successfully!'));
                    });
                });
            }
        });
    }
    signinOld = (req, res) => {
        User.findOne({
                username: req.body.username
            })
            .populate("roles", "-__v")
            .exec((err, user) => {
                if (err) {
                    res.status(500).send(getMessageForClient(err));
                    return;
                }
                if (!user) {
                    return res.status(404).send(res.statusCode, getMessageForClient('User was not found!'));
                }
                var passwordIsValid = bcrypt.compareSync(
                    req.body.password,
                    user.password
                );
                if (!passwordIsValid) {
                    return res.status(401).send(getMessageForClient(res.statusCode, 'Invalid Password!'));
                }
                var token = jwt.sign({ id: user.id }, config.secret, {
                    expiresIn: 604800 // 7 days
                });
                var authorities = [];
                for (let i = 0; i < user.roles.length; i++) {
                    authorities.push(user.roles[i].name);
                }
                res.status(200).send({
                    id: user._id,
                    username: user.username,
                    email: user.email,
                    roles: authorities,
                    accessToken: token
                });
            });
    }

    signup = async(req, res, next) => {
        const user = new User({
            username: req.body.username,
            email: req.body.email,
            phone_number: req.body.phone_number,
            password: bcrypt.hashSync(req.body.password, 8)
        });
        try {
            if (req.body.roles) {
                const roles = await Role.find({ name: { $in: req.body.roles } });
                user.roles = roles.map(role => role._id);
                user.save();
                res.send(getMessageForClient(res.statusCode, 'User was registered successfully!'));
            } else {
                const role = await Role.findOne({ name: "user" });
                user.roles = [role._id];
                user.save();
                res.send(getMessageForClient(res.statusCode, 'User was registered successfully!'));
            }
        } catch (err) {
            res.status(500).send(getMessageForClient(res.statusCode, err));
        }
    }

    signin = async(req, res) => {
        try {
            const user = await User.findOne({
                    username: req.body.username
                })
                .populate("roles", "-__v");

            if (user) {
                const passwordIsValid = bcrypt.compareSync(
                    req.body.password,
                    user.password
                );
                if (!passwordIsValid) {
                    return res.status(401).send(res.statusCode, getMessageForClient(res.statusCode, 'Invalid Password!'));
                }
                const token = jwt.sign({ id: user.id }, config.secret, {
                    expiresIn: 604800 // 7 days
                });
                let authorities = [];
                for (let i = 0; i < user.roles.length; i++) {
                    authorities.push(user.roles[i].name);
                }
                res.status(200).send({
                    id: user._id,
                    username: user.username,
                    email: user.email,
                    phone_number: user.phone_number,
                    roles: authorities,
                    accessToken: token
                });
            } else {
                return res.status(404).send(getMessageForClient(res.statusCode, 'User was not found!'));
            }
        } catch (error) {
            res.status(500).send(getMessageForClient(res.statusCode, err));
        }
    }
}

module.exports = new AuthController();