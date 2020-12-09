const Role = require('../app/model/Role');
const User = require('../app/model/User');

const ROLES = ['admin', 'customer', 'shipper'];

const getMessageForClient = require('../utils/message');

checkDuplicateUsernameOrEmail = async(req, res, next) => {
    try {
        //username
        const user1 = await User.findOne({ username: req.body.username });
        if (user1) {
            res.status(400).send(getMessageForClient(res.statusCode, 'Failed! Username is already in use!'));
            return;
        }

        //email
        const user2 = await User.findOne({ email: req.body.email });
        if (user2) {
            res.status(400).send(getMessageForClient(res.statusCode, 'Failed! Email is already in use!'));
            return;
        }
        next();
    } catch (error) {
        res.status(500).send(getMessageForClient(res.statusCode, error));
    }
}


checkRolesExisted = async(req, res, next) => {
    if (Array.isArray(req.body.roles)) {
        for (let i = 0; i < req.body.roles.length; i++) {
            const role = await Role.findOne({ name: req.body.roles[i] });
            if (!role) {
                res.status(400).send(getMessageForClient('Failed! Role [' + req.body.roles[i] + '] does not exist!'));
                return;
            }
        }
        next();
        return;
    }
    res.status(400).send(getMessageForClient(res.statusCode, 'Failed! Roles must be an string array!'));
};

const verifySignUp = {
    checkDuplicateUsernameOrEmail,
    checkRolesExisted
};


module.exports = verifySignUp;