/**
 * @module
 * @author zccz14 <zccz14@outlook.com>
 */
const mongoose = require('mongoose');
const Schema = mongoose.Schema;
const IsEmail = require('isemail');

const UserRoleSchema = require('./user_role');
/**
 * @class User
 */
const UserSchema = new Schema({
  /**
   * @member 
   * @desc 用户名 不可重复
   * @type {String}
   */
  username: { type: String, required: true, unique: true },
  /**
   * @member
   * @desc 电子邮件 不可重复
   * @type {String}
   */
  email: {
    type: String,
    required: true,
    validate: { validator: IsEmail.validate, message: 'email illegal' },
    unique: true
  },
  /**
   * @member
   * @desc 密码 存储时会被加密
   * @type {String}
   */
  password: {
    type: String,
    required: true,
    minlength: 7,
    validate: {
      validator: function (v) { return v.match(/[a-z]/) && v.match(/\d/); },
      message: 'password illegal'
    }
  },
  roles: [UserRoleSchema],
  admin: { type: Boolean, default: false },
  InvitedTo: [Schema.Types.ObjectId],
  problems: [Schema.Types.ObjectId],
  /**
   * @member
   * @desc 用户作为裁判时的代理URL
   * @type {String}
   */
  judgeProxy: { type: String },
  /**
   * @member
   * @desc 用户作为裁判时的尚未判决的问题
   * @type {ObjectId[]}
   */
  judgeList: [Schema.Types.ObjectId]
});

module.exports = UserSchema;