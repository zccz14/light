// 配置查阅索引（重要）
// system.mongodb: http://mongodb.github.io/node-mongodb-native/2.2/reference/connecting/connection-settings/
// system.morgan: https://github.com/expressjs/morgan
// system.session: https://github.com/expressjs/session
const configuration = {
    system: {
        mongodb: {
            URI: "mongodb://localhost:27017/OrangeJuice"
        },
        morgan: {
            format: 'dev',
            options: {}
        },
        session: {
            secret: 'ORANGEJUICE',
            key: 'OJID',
            resave: false,
            saveUninitialized: true
        },
        defaultPort: 80
    }
}

module.exports = configuration;