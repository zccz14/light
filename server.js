// 引入外部库
const express = require('express');
const morgan = require('morgan');
const bodyParser = require('body-parser');
const cookieParser = require('cookie-parser');
const session = require('express-session');
const path = require('path');
const mongoose = require('mongoose');
// 引入配置
const configuration = require('./config');
// 连接数据库
mongoose.connect(configuration.system.mongodb.URI);

var server = express();
// 注册
server.use(morgan(configuration.system.morgan.format, configuration.system.morgan.options));
server.use(bodyParser.json());
server.use(bodyParser.urlencoded({ extended: false }));
server.use(cookieParser());
server.use(session(configuration.system.session));
// 注册路由
server.use(express.static(configuration.system.assets.path));
server.use('/user', require('./routes/user'));
server.use('/group', require('./routes/group'));

// 导出服务器
module.exports = server;