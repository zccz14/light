# User Retrieve

在所有用户中**查询符合条件**的用户。

除了个别隐私域，如 password，都开放给前端自定义搜索。

目前仅支持精确检索。

## 请求

### 查询


| 路径    | 说明                                   | 备注           |
| ----- | ------------------------------------ | ------------ |
| limit | 单次查询用户个数                             | 默认为15        |
| skip  | 跳过若干个用户                              | 默认为0         |
| ...   | 见 [UserSchema](../../models/user.js) | password 域除外 |

## 响应

### 正文

| 路径    | 说明      | 备注   |
| ----- | ------- | ---- |
| users | 用户文档的数组 |      |

## 样例

若有用户集合：

```json
[
    {
        "_id": "585821bd90154107c0234139",
        "username": "zccz14",
        "email": "a@a.com",
    },
    {
        "_id": "585821c790154107c023413a",
        "username": "zccz15",
        "email": "ab@a.com",
    }
]
```

则查询 `?limit=1` 时与查询`?username=zccz14`得到响应均为：

```json
{
    "code": 0,
    "users": [
        {
            "_id": "585821bd90154107c0234139",
            "username": "zccz14",
            "email": "a@a.com",
        }
    ]
}
```

## 相关

+ [User Create - 创建用户](user_create.md)