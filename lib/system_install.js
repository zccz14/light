const co = require('co');
const mongoose = require('mongoose');
const ObjectId = mongoose.Types.ObjectId;
const request = require('superagent');

const System = require('../models/system');
const User = require('../models/user');
const Group = require('../models/group');

const OnError = require('./on_error');

const config = require('../config');

const responseOnInstalled = {
    code: 7,
    msg: 'installed'
};

function SystemInstall(req, res, next) {
    co(function* () {
        let count = yield System.count({}).exec();
        if (count !== 0) {
            res.json(responseOnInstalled);
            return;
        }
        // create first administrator
        let user = new User({
            username: (req.body.username || '').trim(),
            email: (req.body.email || '').trim(),
            password: req.body.password || ''
        });
        user = yield user.save();
        user.password = config.system.passwordHash.store(user.password);
        user = yield user.save();
        user.password = undefined;
        let system = new System({
            administrators: [user._id]
        });
        yield system.save();
        // create public group

        console.log('[system] System Installed');
        res.json({
            code: 0,
            msg: 'system installed',
            user
        });
    }).catch(OnError(req, res));
}

module.exports = SystemInstall;