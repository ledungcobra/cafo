const jwt = require("jsonwebtoken");
const config = require("../config/auth/auth");
const Role = require('../app/model/Role');
const User = require('../app/model/User');

const getMessageForClient = require('../utils/message');

verifyToken = (req, res, next) => {
    let token = req.headers['x-cafo-client-access-token'];
    if (!token) {
        return res.status(403).send(getMessageForClient(res.statusCode, 'No token provided!'));
    }
    jwt.verify(token, config.secret, (err, decoded) => {
        if (err) {
            return res.status(401).send(getMessageForClient(res.statusCode, 'Unauthorized'));
        }
        req.userID = decoded.id;
        next();
    });
};

isAdmin = async(req, res, next) => {
    try {
        const user = await User.findById(req.userID);
        const roles = await Role.find({ _id: { $in: user.roles } });
        for (let i = 0; i < roles.length; i++) {
            if (roles[i].name === 'admin') {
                req.permision = 'admin';
                next();
                return;
            }
        }
        res.status(403).send(getMessageForClient(res.statusCode, 'Require [admin] Role!'));
        return;

    } catch (error) {
        res.status(500).send(getMessageForClient(res.statusCode, error));
    }
};


isCustomer = async(req, res, next) => {
    try {
        const user = await User.findById(req.userID);
        const roles = await Role.find({ _id: { $in: user.roles } });
        for (let i = 0; i < roles.length; i++) {
            if (roles[i].name === 'customer') {
                req.permision = 'customer';
                next();
                return;
            }
        }
        res.status(403).send(getMessageForClient(res.statusCode, 'Require [customer] role!'));
        return;

    } catch (error) {
        res.status(500).send(getMessageForClient(res.statusCode, error));
    }
};



isShipper = async(req, res, next) => {
    try {
        const user = await User.findById(req.userID);
        const roles = await Role.find({ _id: { $in: user.roles } });
        for (let i = 0; i < roles.length; i++) {
            if (roles[i].name === 'shipper') {
                req.permision = 'shipper';
                next();
                return;
            }
        }
        res.status(403).send(getMessageForClient(res.statusCode, 'Require [shipper] role!'));
        return;

    } catch (error) {
        res.status(500).send(getMessageForClient(res.statusCode, error));
    }
};

const authJWT = {
    verifyToken,
    isAdmin,
    isCustomer,
    isShipper
};
module.exports = authJWT;