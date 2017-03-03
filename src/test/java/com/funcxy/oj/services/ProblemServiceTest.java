
//package com.funcxy.oj.services;
//
//import com.funcxy.oj.Application;
//import com.funcxy.oj.models.Problem;
//import com.funcxy.oj.models.User;
//import org.bson.types.ObjectId;
//import org.junit.After;
//import org.junit.Assert;
//import org.junit.BeforeClass;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.SpringBootConfiguration;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
///**
// * Created by wtupc96 on 2017/3/1.
// */

//
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest(classes = Application.class)
//@SpringBootConfiguration

//public class ProblemServiceTest {

//    private static final User USER = new User();
//    private static final String INVALID_TYPE_1 = null;
//    private static final String INVALID_TYPE_2 = "";
//    private static final String INVALID_TYPE_3 = "      ";
//    private static final String VALID_TYPE = "  Test_Type";
//    private static final String INVALID_TITLE_1 = null;
//    private static final String INVALID_TITLE_2 = "";
//    private static final String INVALID_TITLE_3 = "      ";
//    private static final String VALID_TITLE = "Test_Title   ";
//    private static final String INVALID_DESCRIPTION_1 = null;
//    private static final String INVALID_DESCRIPTION_2 = "";
//    private static final String INVALID_DESCRIPTION_3 = "      ";
//    private static final String VALID_DESCRIPTION = "Test _ Description";
//    private static final String INVALID_REFERENCE_ANSWER_1 = null;
//    private static final String INVALID_REFERENCE_ANSWER_2 = "";
//    private static final String INVALID_REFERENCE_ANSWER_3 = "      ";
//    private static final String VALID_REFERENCE_ANSWER = "Test_Re fe re n c e A n s wer";
//    private static Problem problem;
//
//    @Autowired
//    private ProblemService problemService;
//
//    @BeforeClass
//    public static void createProblem() {
//        problem = new Problem();
//        problem.setId(ObjectId.get());
//        problem.setCreator(USER);
//        problem.setType(VALID_TYPE);
//        problem.setTitle(VALID_TITLE);
//        problem.setDescription(VALID_DESCRIPTION);
//        problem.setReferenceAnswer(VALID_REFERENCE_ANSWER);
//    }
//
//    @Test
//    public void testValidProblemService() {
//        try {
//            Problem problem1 = problemService.save(problem);
//            Assert.assertEquals("Problems ocurrs when comparing creator Id.", problem1.getCreator(), USER);
//            Assert.assertEquals("Problems ocurrs when comparing title.", problem1.getTitle(), VALID_TITLE.trim());
//            Assert.assertEquals("Problems ocurrs when comparing type.", problem1.getType(), VALID_TYPE.trim());
//            Assert.assertEquals("Problems ocurrs when comparing description.", problem1.getDescription(), VALID_DESCRIPTION.trim());
//            Assert.assertEquals("Problems ocurrs when comparing reference answer.", problem1.getReferenceAnswer(), VALID_REFERENCE_ANSWER.trim());
//        } catch (Exception e) {
//            System.out.println("Saving process occurs an Exception.");
//            e.printStackTrace();
//        }
//    }
//
//    @Test(expected = Exception.class)
//    public void testInvalidProblemService1() throws Exception {
//        problem.setType(INVALID_TYPE_1);
//        problem.setTitle(INVALID_TITLE_1);
//        problem.setDescription(INVALID_DESCRIPTION_1);
//        problem.setReferenceAnswer(INVALID_REFERENCE_ANSWER_1);
//        Problem problem1 = problemService.save(problem);
//        Assert.assertEquals("Problems ocurrs when comparing creator Id.", problem1.getCreator(), USER);
//        Assert.assertEquals("Problems ocurrs when comparing title.", problem1.getTitle(), INVALID_TITLE_1.trim());
//        Assert.assertEquals("Problems ocurrs when comparing type.", problem1.getType(), INVALID_TYPE_1.trim());
//        Assert.assertEquals("Problems ocurrs when comparing description.", problem1.getDescription(), INVALID_DESCRIPTION_1.trim());
//        Assert.assertEquals("Problems ocurrs when comparing reference answer.", problem1.getReferenceAnswer(), INVALID_REFERENCE_ANSWER_1.trim());
//    }
//
//    @Test(expected = Exception.class)
//    public void testInvalidProblemService2() throws Exception {
//        problem.setType(INVALID_TYPE_2);
//        problem.setTitle(INVALID_TITLE_2);
//        problem.setDescription(INVALID_DESCRIPTION_2);
//        problem.setReferenceAnswer(INVALID_REFERENCE_ANSWER_2);
//        Problem problem1 = problemService.save(problem);
//        Assert.assertEquals("Problems ocurrs when comparing creator Id.", problem1.getCreator(), USER);
//        Assert.assertEquals("Problems ocurrs when comparing title.", problem1.getTitle(), INVALID_TITLE_2.trim());
//        Assert.assertEquals("Problems ocurrs when comparing type.", problem1.getType(), INVALID_TYPE_2.trim());
//        Assert.assertEquals("Problems ocurrs when comparing description.", problem1.getDescription(), INVALID_DESCRIPTION_2.trim());
//        Assert.assertEquals("Problems ocurrs when comparing reference answer.", problem1.getReferenceAnswer(), INVALID_REFERENCE_ANSWER_2.trim());
//    }
//
//    @Test(expected = Exception.class)
//    public void testInvalidProblemService3() throws Exception {
//        problem.setType(INVALID_TYPE_3);
//        problem.setTitle(INVALID_TITLE_3);
//        problem.setDescription(INVALID_DESCRIPTION_3);
//        problem.setReferenceAnswer(INVALID_REFERENCE_ANSWER_3);
//        Problem problem1 = problemService.save(problem);
//        Assert.assertEquals("Problems ocurrs when comparing creator Id.", problem1.getCreator(), USER);
//        Assert.assertEquals("Problems ocurrs when comparing title.", problem1.getTitle(), INVALID_TITLE_3.trim());
//        Assert.assertEquals("Problems ocurrs when comparing type.", problem1.getType(), INVALID_TYPE_3.trim());
//        Assert.assertEquals("Problems ocurrs when comparing description.", problem1.getDescription(), INVALID_DESCRIPTION_3.trim());
//        Assert.assertEquals("Problems ocurrs when comparing reference answer.", problem1.getReferenceAnswer(), INVALID_REFERENCE_ANSWER_3.trim());
//    }
//
//    @After
//    public void disposeProblem() {
//        problemService.delete(problem.getId());
//    }

//}

