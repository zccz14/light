module.exports =
    require('express')
        .Router()
        .post('/', require('../lib/submission_create'))
        .get('/', require('../lib/submission_retrieve'))
        .get('/:submissionId', require('../lib/submission_detail'))
        .put('/:submissionId', require('../lib/submission_sentence_update'))