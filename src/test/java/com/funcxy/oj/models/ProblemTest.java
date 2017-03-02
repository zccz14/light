package com.funcxy.oj.models;

import com.funcxy.oj.Application;
import com.funcxy.oj.models.Problem;
import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by wtupc96 on 2017/3/1.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@SpringBootConfiguration
public class ProblemTest {
    private static Problem problem;
    private static final ObjectId CREATOR_ID = ObjectId.get();
    private static final String TYPE = "Test_Type";
    private static final String TITLE = "Test_Title";
    private static final String DESCRIPTION = "Test_Description";
    private static final String REFERENCE_ANSWER = "Test_ReferenceAnswer";

    @BeforeClass
    public static void createAProblem(){
        problem = new Problem();
        problem.setCreatorId(CREATOR_ID);
        problem.setType(TYPE);
        problem.setTitle(TITLE);
        problem.setDescription(DESCRIPTION);
        problem.setReferenceAnswer(REFERENCE_ANSWER);
    }

    @Test
    public void testProblem(){
        Assert.assertEquals("OK", problem.getCreatorId(), CREATOR_ID);
        Assert.assertEquals("OK", problem.getTitle(), TITLE);
        Assert.assertEquals("OK", problem.getDescription(), DESCRIPTION);
        Assert.assertEquals("OK", problem.getType(), TYPE);
        Assert.assertEquals("OK", problem.getReferenceAnswer(), REFERENCE_ANSWER);
    }
}
