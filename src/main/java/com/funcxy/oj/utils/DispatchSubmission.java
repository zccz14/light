package com.funcxy.oj.utils;

import com.funcxy.oj.models.Submission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.concurrent.Future;

/**
 * @author  aak1247 on 2017/3/14.
 */
@Service
public class DispatchSubmission {
    RestTemplate restTemplate;
    public Submission dispatchSubmission(Submission submission, URI uri){
        restTemplate = new RestTemplate();
        HttpEntity<Submission> request = new HttpEntity<>(submission);
        try {
            return restTemplate.postForObject(uri,request,Submission.class);
        }catch (Exception e){
            System.out.println(e);
        }
        return null;
    }
}
