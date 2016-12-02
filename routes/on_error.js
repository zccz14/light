module.exports = function(res) {
    return function(err) {
        if (err.errors) {
            res.json({
                code: 2,
                errors: err.errors
            });
        } else if (err.code == 11000) {
            res.json({
                code: 3,
                errors: err.errmsg
            });
        } else {
            res.json({
                code: 1
            });
        }
    };
};