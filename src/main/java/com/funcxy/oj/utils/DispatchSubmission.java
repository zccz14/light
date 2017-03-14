package com.funcxy.oj.utils;

import com.funcxy.oj.contents.SubmissionWithToken;
import com.funcxy.oj.models.Submission;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.concurrent.Future;

/**
 * @author aak1247 on 2017/3/14.
 */
@Service
public class DispatchSubmission {

    private final RestTemplate restTemplate;

    public DispatchSubmission(RestTemplateBuilder restTemplateBuilder){
        this.restTemplate = restTemplateBuilder.build();
    }

    @Async
    public Future<Submission> dispatchSubmission(SubmissionWithToken submissionWithToken, String url) throws  InterruptedException{
        HttpEntity<SubmissionWithToken> request = new HttpEntity<>(submissionWithToken);
        Submission results = restTemplate.postForObject(url,request,Submission.class);
        Thread.sleep(1000L);
        return new AsyncResult<>(results);
    }
}
