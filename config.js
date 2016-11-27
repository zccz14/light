// 配置查阅索引（重要）
// system.mongodb: http://mongodb.github.io/node-mongodb-native/2.2/reference/connecting/connection-settings/
// system.morgan: https://github.com/expressjs/morgan
// system.session: https://github.com/expressjs/session
const configuration = {
    user: {
        password: {
            minimumLength: 7,
            minimumLowercaseLetter: 1,
            minimumNumeral: 1
        }
    },
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
        defaultPort: 2048
    }
}

module.exports = configuration;