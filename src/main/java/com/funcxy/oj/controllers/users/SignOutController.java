package com.funcxy.oj.controllers.users;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * Sign Out API
 *
 * @author zccz14
 */
@RestController
@RequestMapping("/users/sign-out")
public class SignOutController {
    @RequestMapping(method = GET)
    public ResponseEntity<Object> SignOut(HttpSession httpSession) {
        httpSession.invalidate();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
