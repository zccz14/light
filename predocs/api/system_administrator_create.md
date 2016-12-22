# System Administrator Create

授予其他**已经存在的**用户**系统管理员**权限。

是将请求正文中的 userId 添加到 system.administrators 集合中，保证不会重复添加。

在重复添加时也不会发出警告。

仅有系统管理员可以请求这个API。

## 请求

### 正文


| 路径     | 说明   | 备注            |
| ------ | ---- | ------------- |
| userId | 用户ID | BSON ObjectID |

## 响应

### 正文

| 路径     | 说明           | 备注                         |
| ------ | ------------ | -------------------------- |
| system | 更新后的system状态 | system.administrators 可能变化 |

## 样例

## 相关

+ [User Create - 创建用户](user_create.md)