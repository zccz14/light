const configuration = require('./config');
const mongodb = require('mongodb');
mongodb.MongoClient.connect(configuration.system.mongodb.URI, {}, (err, db) => {
    if (err) throw err;
    console.log('Connect MongoDB Server Successfully');
    module.exports.collection = function (name) {
        return db.collection(name);
    }
});