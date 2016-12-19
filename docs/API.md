# API 文档概览
## 基本属性

每个 API 分别对 请求/响应 进行说明。

请求中主要包含查询 (query) 、参数 (params) 与正文 (body) 。 

响应中主要包含正文 (body) 。

## 错误码

**任何** API 的**响应正文**中都应至少包含一个 `code` 域，其值与含义如下表所示：

| 代码   | 说明                   | 备注               |
| ---- | -------------------- | ---------------- |
| 0    | OK                   | 操作成功。            |
| 1    | BUG                  | BUG 出现，请提 Issue。 |
| 2    | Validation Failed    | 非法的数据、缺少必填项等     |
| 3    | Data Duplicated      | 唯一的名字重复等         |
| 5    | Wrong Passport       | 用户名或密码错误         |
| 7    | Permission Denied    | 权限不足             |
| 11   | Resource Unavailable | 资源不可用            |



## Next

+ [用户 API](api/user.md)