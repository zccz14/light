package com.funcxy.oj;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by aak12 on 2017/2/27.
 */
@RestController
public class HelloController {
    @RequestMapping("/")
    public String index(){
        return "hello";
    }
}
