package com.funcxy.oj.models;

import com.funcxy.oj.Application;
import com.funcxy.oj.repositories.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by lqp on 2017/3/1.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class SubmissionTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    public void TestXXX() {
        User a = new User();
        a.setUsername("111");
        userRepository.save(a);
        assert (userRepository.findByUsername("111") != null);
    }
}
