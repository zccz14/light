module.exports = {
    signIn: function (req, res, next) {
        if (req.session.user) {
            next();
        } else {
            res.json({ code: 7 });
        }
    },
    admin: function (req, res, next) {
        if (req.session.user.admin) {
            next();
        } else {
            res.json({ code: 7 });
        }
    }
};