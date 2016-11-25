# Full API Document
## Error Code
All the error code are **primes** except 0 for OK. [See Also](../errors/index.js)

## User API
### User Sign Up
POST `/user` and send:

```json
{
    "email": "sample@function-x.org",
    "password": "a123456"
}
```

possible error code:

+ 2: illegal email format
+ 3: password too short
+ 5: password lowercase letter limit
+ 7: password numeral limit
+ 11: the email has been used
+ 13: require field

email will be refused if it's illegal.

configuration at `user.password` to change the behavior of password limits

### User Sign In
POST `/user/sign-in` and send:

```json
{
    "email": "sample@function-x.org",
    "password": "a123456"
}
```

possible error code:

+ 2: illegal email format
+ 13: require field
+ 17: wrong email or password
+ 19: user not found

### User Sign Out
GET `/user/sign-out`

expect the error code to equal `0`.

### User Profile
GET `/user/profile`

Fetch the current user information.

response json:

```js
{
    "code": 0,
    "body": {
        // user profile
    }
}
```

possible error code:

+ 23: not signin yet