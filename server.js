// 引入外部库
const express = require('express');
const morgan = require('morgan');
const bodyParser = require('body-parser');
const cookieParser = require('cookie-parser');
const session = require('express-session');
const path = require('path');
const mongoose = require('mongoose');
mongoose.Promise = Promise;
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

server.use(function(req, res, next) {
    res.header('Access-Control-Allow-Origin', 'http://127.0.0.1:8080');
    res.header('Access-Control-Allow-Headers', 'Content-Type');

    next();
})

// 注册路由
server.use(express.static(configuration.system.assets.path));
server.use('/user', require('./routes/user'));
server.use('/group', require('./routes/group'));
server.use('/problem_list',require('./routes/problem_list'))
server.use('/problem', require('./routes/problem'));
server.use('/system', require('./routes/system'));

// 导出服务器
module.exports = server;
