// Index.js 核心功能注冊
// 引入外部库
const express = require('express');
const morgan = require('morgan');
const bodyParser = require('body-parser');
const cookieParser = require('cookie-parser');
const session = require('express-session');
const path = require('path');
// 引入配置
const configuration = require('./config');

// 定义 APP Server
var app = express();
// 端口注册
app.set('port', process.env.PORT || configuration.system.defaultPort);

// 注册
app.use(morgan(configuration.system.morgan.format, configuration.system.morgan.options));
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(session(configuration.system.session));
// 注册路由
app.use(express.static(path.join(__dirname, 'public')));

// 监听端口
app.listen(app.get('port'), () => {
    console.log('Orange Juice Server');
});