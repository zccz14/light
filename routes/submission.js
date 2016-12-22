module.exports =
    require('express').Router().post('/', require('../lib/submission_create'))