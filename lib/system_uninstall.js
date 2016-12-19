const co = require('co');

const mongoose = require('mongoose');

const OnError = require('./on_error');

function SystemUninstall(req, res, next) {
    co(function* () {
        let result = yield mongoose.connection.db.dropDatabase();
        if (result !== true) throw result;
        res.json({ code: 0 });
    }).catch(OnError(req, res));
}

module.exports = SystemUninstall;