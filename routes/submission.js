module.exports = require('express')
                     .Router()
                     .post('/', require('../lib/submission_create'))
                     .get('/', require('../lib/submission_retrieve'))