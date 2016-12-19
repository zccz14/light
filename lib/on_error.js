const co = require('co');

const OnError = (req, res) => (err) => {
    if (err.errors) {
        // validation failed
        res.json({
            code: 2,
            errors: err.errors
        });
    } else if (err.code == 11000) {
        // duplicated
        res.json({
            code: 3,
            errors: err.errmsg
        });
    } else {
        // BUG
        console.log(err);
        res.json({
            code: 1,
            msg: 'BUG'
        });
    }
}

module.exports = OnError;