package com.funcxy.light.controllers.users;

import com.funcxy.light.errors.ForbiddenError;
import com.funcxy.light.utils.UserUtil;
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
        if (!UserUtil.isSignedIn(httpSession)){
            return new ResponseEntity<>(new ForbiddenError(),HttpStatus.FORBIDDEN);
        }
        httpSession.invalidate();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
