package com.funcxy.oj.controllers.users;

import com.funcxy.oj.models.User;
import com.funcxy.oj.repositories.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

import static com.funcxy.oj.utils.UserUtil.isSignedIn;

/**
 * Created by DDHEE on 2017/3/7.
 */

@RestController
@RequestMapping("/users")
public class UserPrfController {
    @Autowired
    UserRepository userRepository;
    //收藏问题
    @RequestMapping(value = "/{username}/liked-problems/{id}", method = RequestMethod.POST)
    public ResponseEntity<Object> likeProblem(@PathVariable String username, @PathVariable ObjectId id, HttpSession httpSession) {
        if (!isSignedIn(httpSession))
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        userRepository
                .findById((ObjectId) httpSession.getAttribute("userId"))
                .addProblemLiked(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    //收藏题单
    @RequestMapping(value = "/{userneme}/liked-problem-lists/{id}", method = RequestMethod.POST)
    public ResponseEntity<Object> likeProblemList(@PathVariable String username, @PathVariable ObjectId id, HttpSession httpSession) {
        if (!isSignedIn(httpSession))
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        userRepository
                .findById((ObjectId) httpSession.getAttribute("userId"))
                .addProblemListLiked(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    //取消收藏问题
    @RequestMapping(value = "/{username}/liked-problems/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> unlikeProblem(@PathVariable String username, @PathVariable ObjectId id, HttpSession httpSession) {
        if (!isSignedIn(httpSession))
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        userRepository
                .findById((ObjectId) httpSession.getAttribute("userId"))
                .deleteProblemLiked(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    //取消收藏题单
    @RequestMapping(value = "/{username}/liked-problem-lists/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> unlikeProblemLists(@PathVariable String username, @PathVariable ObjectId id, HttpSession httpSession) {
        if (!isSignedIn(httpSession))
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        userRepository
                .findById((ObjectId) httpSession.getAttribute("userId"))
                .deleteProblemListLiked(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    //获取收藏的题单
    @RequestMapping(value = "/{username}/liked-problems", method = RequestMethod.GET)
    public ResponseEntity<Object> likedProblems(@PathVariable String username, HttpSession httpSession) {
        if (!isSignedIn(httpSession))
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        User user = userRepository.findById((ObjectId) httpSession.getAttribute("userId"));
        return new ResponseEntity(user.getProblemLiked(), HttpStatus.OK);
    }

    //获取收藏的题目
    @RequestMapping(value = "/{username}/liked-problem-lists", method = RequestMethod.GET)
    public ResponseEntity<Object> likedProblemLists(@PathVariable String username, HttpSession httpSession) {
        if (!isSignedIn(httpSession))
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        User user = userRepository.findById((ObjectId) httpSession.getAttribute("userId"));
        return new ResponseEntity(user.getProblemListLiked(), HttpStatus.OK);
    }

}
