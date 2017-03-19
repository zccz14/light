package com.funcxy.light.controllers;

import com.funcxy.light.errors.ForbiddenError;
import com.funcxy.light.models.User;
import com.funcxy.light.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * Session Controller
 *
 * @author zccz14
 */
@RestController
@RequestMapping("/session")
public class SessionController {
    /**
     * 进行Users的数据库操作
     */
    private final
    UserRepository userRepository;

    /**
     * 构造函数
     *
     * @param userRepository 用户仓库
     */
    @Autowired
    public SessionController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @RequestMapping(value = "/user", method = GET)
    public ResponseEntity GetSession(HttpSession httpSession) {
        String userId = (String) httpSession.getAttribute("userId");
        if (userId == null) {
            return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
        }
        User user = userRepository.findById(userId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
