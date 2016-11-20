// 配置查阅索引（重要）
// system.morgan: https://github.com/expressjs/morgan
// system.session: https://github.com/expressjs/session
const configuration = {
    system: {
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
        port: 80
    }
}

module.exports = configuration;