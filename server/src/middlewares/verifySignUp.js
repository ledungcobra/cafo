const Role = require('../app/model/Role');
const User = require('../app/model/User');

const ROLES = ['admin', 'customer', 'shipper'];

const getMessageForClient = require('../utils/message');

checkDuplicateUsernameOrEmail = (req, res, next) => {
    // Username
    User.findOne({
        username: req.body.username
    }).exec((err, user) => {
        if (err) {
            res.status(500).send(getMessageForClient(err));
            return;
        }
        if (user) {
            res.status(400).send(getMessageForClient('Failed! Username is already in use!'));
            return;
        }
        // Email
        User.findOne({
            email: req.body.email
        }).exec((err, user) => {
            if (err) {
                res.status(500).send(getMessageForClient(err));
                return;
            }
            if (user) {
                res.status(400).send(getMessageForClient('Failed! Email is already in use!'));
                return;
            }
            next();
        });
    });
};


checkRolesExisted = async(req, res, next) => {
    if (req.body.roles) {
        for (let i = 0; i < req.body.roles.length; i++) {
            const role = await Role.findOne({ name: req.body.roles[i] });
            if (!role) {
                res.status(400).send(getMessageForClient('Failed! Role ' + req.body.roles[i] + 'does not exist!'));
                return;
            }
        }
    }
    next();
};

const verifySignUp = {
    checkDuplicateUsernameOrEmail,
    checkRolesExisted
};


module.exports = verifySignUp;