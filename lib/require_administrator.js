const co = require('co');

const System = require('../models/system');

const OnError = require('./on_error');

const response = {
    code: 7,
    msg: 'require administrator'
};

function RequireAdministrator(req, res, next) {
    co(function* () {
        let system = yield System.findOne().exec();
        let administrators = system.administrators;
        let userId = req.session.userId;
        if (administrators.some(v => v.toString() === userId.toString())) {
            next();
        } else {
            res.json(response);
        }
    }).catch(OnError(req, res));
}

module.exports = RequireAdministrator;