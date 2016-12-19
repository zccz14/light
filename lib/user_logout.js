function UserLogout(req, res, next) {
    req.session.destroy(function (err) {
        if (err) throw err;
        res.json({
            code: 0
        });
    });
}

module.exports = UserLogout;