const co = require('co');

const OnError = (req, res) => (err) => {
    let isMatched = false;
    // error router
    if (err instanceof Error) {
        if (err.name === 'ValidationError') {
            isMatched = true;
            res.json({
                code: 2,
                message: err.message,
                errors: err.errors
            });
        } else if (err.name === 'MongoError') {
            if (err.code === 11000) {
                isMatched = true;
                // duplicated
                res.json({
                    code: 3,
                    message: err.message,
                    errors: err.errmsg
                });
            }
        } else if (err.name === 'Error') {
            if (err.message === 'Argument passed in must be a single String of 12 bytes or a string of 24 hex characters') {
                res.json({
                    code: 2,
                    message: 'Invalid ObjectId'
                });
            }
        }
    }
    // uncatched
    if (isMatched === false) {
        console.log('[unexpected error]', err.name, err.message);
        res.json({
            code: 1,
            message: 'BUG'
        });
    }
}

module.exports = OnError;