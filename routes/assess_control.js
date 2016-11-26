module.exports = {
    signIn: function (req, res, next) {
        if (req.session.userId) {
            next();
        } else {
            res.json({ code: 7 });
        }
    }
};