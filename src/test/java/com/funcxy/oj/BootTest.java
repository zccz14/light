package com.funcxy.oj;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Test for Basic Boot
 *
 * @author zccz14
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class BootTest {
    @Test
    public void main() {
        System.out.println("Orange Juice Boot Test");
    }
}
