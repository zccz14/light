const response = {
    code: 7,
    msg: 'require administrator'
};
function RequireAdministrator(req, res, next) {
    if (req.session.user.admin) {
        next();
    } else {
        res.json(response);
    }
}

module.exports = RequireAdministrator;