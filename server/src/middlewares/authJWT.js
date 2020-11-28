const jwt = require("jsonwebtoken");
const config = require("../config/auth/auth");
const Role = require('../app/model/Role');
const User = require('../app/model/User');

const getMessageForClient = require('../utils/message');

verifyToken = (req, res, next) => {
    let token = req.headers['x-cafo-client-access-token'];
    if (!token) {
        return res.status(403).send(getMessageForClient('No token provided!'));
    }
    jwt.verify(token, config.secret, (err, decoded) => {
        if (err) {
            return res.status(401).send(getMessageForClient('Unauthorized'));
        }
        req.userID = decoded.id;
        next();
    });
};

isAdmin = (req, res, next) => {
    User.findById(req.userID).exec((err, user) => {
        if (err) {
            res.status(500).send(getMessageForClient(err));
            return;
        }
        Role.find({
                _id: { $in: user.roles }
            },
            (err, roles) => {
                if (err) {
                    res.status(500).send(getMessageForClient(err));
                    return;
                }
                for (let i = 0; i < roles.length; i++) {
                    if (roles[i].name === 'admin') {
                        req.permision = 'admin';
                        next();
                        return;
                    }
                }
                res.status(403).send(getMessageForClient('Require Admin Role!'));
                return;
            }
        );
    });
};


isCustomer = (req, res, next) => {
    User.findById(req.userID).exec((err, user) => {
        if (err) {
            res.status(500).send(getMessageForClient(err));
            return;
        }
        Role.find({
                _id: { $in: user.roles }
            },
            (err, roles) => {
                if (err) {
                    res.status(500).send(getMessageForClient(err));
                    return;
                }
                for (let i = 0; i < roles.length; i++) {
                    if (roles[i].name === 'customer') {
                        req.permision = 'customer';
                        next();
                        return;
                    }
                }
                res.status(403).send(getMessageForClient('Require Customer Role!'));
                return;
            }
        );
    });
};



isShipper = (req, res, next) => {
    User.findById(req.userID).exec((err, user) => {
        if (err) {
            res.status(500).send(getMessageForClient(err));
            return;
        }
        Role.find({
                _id: { $in: user.roles }
            },
            (err, roles) => {
                if (err) {
                    res.status(500).send(getMessageForClient(err));
                    return;
                }
                for (let i = 0; i < roles.length; i++) {
                    if (roles[i].name === 'shipper') {
                        req.permision = 'shipper';
                        next();
                        return;
                    }
                }
                res.status(403).send(getMessageForClient('Require Shipper Role!'));
                return;
            }
        );
    });
};

const authJWT = {
    verifyToken,
    isAdmin,
    isCustomer,
    isShipper
};
module.exports = authJWT;