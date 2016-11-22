# Full API Document
## User Sign Up
POST `/user` and send:

```json
{
    email: String,
    password: String
}
```

email will be refused if it's illegal.

configuration at `user.password` to change the behavior of password limits

## User Sign In
POST `/user/sign-in` and send:

```json
{
    email: String,
    password: String
}
```