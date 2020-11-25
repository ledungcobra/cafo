const jwt = require("jsonwebtoken");
const config = require("../config/auth/auth");
const Role = require('../app/model/Role');
const User = require('../app/model/User');

verifyToken = (req, res, next) => {
    let token = req.headers["x-cafo-client-access-token"];
    if (!token) {
        return res.status(403).send({ message: "No token provided!" });
    }
    jwt.verify(token, config.secret, (err, decoded) => {
        if (err) {
            return res.status(401).send({ message: "Unauthorized!" });
        }
        req.userId = decoded.id;
        //console.log(req.userId)
        next();
    });
};

isAdmin = (req, res, next) => {
    User.findById(req.userId).exec((err, user) => {
        if (err) {
            res.status(500).send({ message: err });
            return;
        }
        Role.find({
                _id: { $in: user.roles }
            },
            (err, roles) => {
                if (err) {
                    res.status(500).send({ message: err });
                    return;
                }
                for (let i = 0; i < roles.length; i++) {
                    if (roles[i].name === "admin") {
                        req.permision = "admin"
                        next();
                        return;
                    }
                }
                return;
                //res.status(403).send({ message: "Require Admin Role!" });
            }
        );
    });
};


isCustomer = (req, res, next) => {
    User.findById(req.userId).exec((err, user) => {
        if (err) {
            res.status(500).send({ message: err });
            return;
        }
        Role.find({
                _id: { $in: user.roles }
            },
            (err, roles) => {
                if (err) {
                    res.status(500).send({ message: err });
                    return;
                }
                for (let i = 0; i < roles.length; i++) {
                    if (roles[i].name === "customer") {
                        next();
                        return;
                    }
                }
                return;
                //res.status(403).send({ message: "Require Moderator Role!" });
            }
        );
    });
};

const authJWT = {
    verifyToken,
    isAdmin,
    isCustomer
};
module.exports = authJWT;