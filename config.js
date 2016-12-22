// 配置查阅索引（重要）
// system.mongodb:
// http://mongodb.github.io/node-mongodb-native/2.2/reference/connecting/connection-settings/
// system.morgan: https://github.com/expressjs/morgan
// system.session: https://github.com/expressjs/session
const configuration =
    {
      user: {
        password:
            {minimumLength: 7, minimumLowercaseLetter: 1, minimumNumeral: 1}
      },
      originFrontEnd: "https://function-x.github.io/",  // Allow API ref
      system: {
        assets: {path: require('path').join(__dirname, 'assets')},
        mongodb: {
          URI: "mongodb://" + (process.env.MONGO_SERVER || "localhost:27017") +
                   "/OrangeJuice"
        },
        morgan: {format: 'dev', options: {}},
        // Store the password in encrypted
        // You can customize the hash algorithm
        passwordHash: {
          store: function(clearPassword) {
            // DO NOT CHANGE IT after deployed!!!
            // Or your users should reset their password
            return require('crypto')
                .createHash('sha1')  // Hash Algorithm
                .update(clearPassword)
                .digest('hex');
          },
          // return true if the clearPassword is true
          verify: function(clearPassword, encryptedPassword) {
            return configuration.system.passwordHash.store(clearPassword) ===
                   encryptedPassword;
          }
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
