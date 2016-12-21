const co = require('co');

const mongoose = require('mongoose');

const System = require('../models/system');

const OnError = require('./on_error');

function SystemUninstall(req, res, next) {
    co(function* () {
        let system = yield System.findOne().exec();
        if (system && system.environment === "production") {
            console.log('[SystemError]', 'Can not uninstall in a production environment');
            res.json({ code: 7 });
        } else {
            let result = yield mongoose.connection.db.dropDatabase();
            res.json({ code: 0 });
        }
    }).catch(OnError(req, res));
}

module.exports = SystemUninstall;