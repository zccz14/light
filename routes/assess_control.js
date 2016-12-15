module.exports = {
    signIn: function (req, res, next) {
        if (req.session.user) {
            next();
        } else {
            res.json({ code: 7 });
        }
    }
};