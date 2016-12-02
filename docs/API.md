# Full API Document
## Error Code

0 for OK, 1 for BUG(if you get a BUG, report it to us please), and others are all **primes**.

[See Also](../errors/index.js)

## User API
### User Sign Up
POST `/user` and send:

```js
{
    "username": String, // passport
    "email": String,
    "password": String
}
```
Actually, `username` and `email` will be trimmed, but `password` won't.

For example, if you post:

```json
{
    "username": "  admin  ",
    "email": " hello@function-x.org",
    "password": " a123456 "
}
```

The API Server will distinguish it as:

```json
{
    "username": "admin",
    "email": "hello@function-x.org",
    "password": " a123456 "
}
```

possible error code:

+ 2: validation failed
+ 3: duplicated

### User Sign In
POST `/user/sign-in` and send:

```js
{
    "username": String,
    "password": String
}
```

possible error code:

+ 5: wrong passport
+ 11: user not found

### User Sign Out
GET `/user/sign-out`

### User Profile
GET `/user/profile`

Fetch the current user information.

response json:

```js
{
    "code": 0,
    "body": UserProfile
}
```

possible error code:

+ 7: authentication failed

### User Update role name
PUT `/user/role/:_id` and send:

The param `_id` is a BSON Object ID. It's the ID of UserRole.

> You can get user profile to get the ID.

```js
{
    "name": ObjectId
}
```

posiible error code:

+ 2: user role name is illegal
+ 3: user role name duplicated
+ 7: not login yet
+ 11: user role not found