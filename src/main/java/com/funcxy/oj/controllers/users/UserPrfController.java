package com.funcxy.oj.controllers.users;

import com.funcxy.oj.controllers.UserController;
import com.funcxy.oj.errors.FieldsDuplicateError;
import com.funcxy.oj.errors.ForbiddenError;
import com.funcxy.oj.errors.NotFoundError;
import com.funcxy.oj.models.User;
import com.funcxy.oj.repositories.UserRepository;
import com.funcxy.oj.utils.UserUtil;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

import java.util.List;
import java.util.stream.Collectors;

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
    @RequestMapping(value = "/{username}/liked-problems/{problemId}", method = RequestMethod.POST)
    public ResponseEntity<Object> likeProblem(@PathVariable String username, @PathVariable ObjectId problemId, HttpSession httpSession) {
        if (!isSignedIn(httpSession))
            return new ResponseEntity<>(new ForbiddenError(),HttpStatus.FORBIDDEN);
        User user = userRepository.findById((ObjectId)httpSession.getAttribute("userId"));
        if (user == null) return new ResponseEntity<>(new NotFoundError(),HttpStatus.NOT_FOUND);
        if (userRepository.findById(
                (ObjectId) httpSession.getAttribute("userId"))
                .getProblemLiked()
                .indexOf(problemId)!=-1){
            return new ResponseEntity<>(new FieldsDuplicateError(),HttpStatus.BAD_REQUEST);
        }
        user.addProblemLiked(problemId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //收藏题单
    @RequestMapping(value = "/{username}/liked-problem-lists/{problemListId}", method = RequestMethod.POST)
    public ResponseEntity<Object> likeProblemList(@PathVariable String username, @PathVariable ObjectId problemListId, HttpSession httpSession) {
        if (!isSignedIn(httpSession))
            return new ResponseEntity<>(new ForbiddenError(),HttpStatus.FORBIDDEN);

        User user = userRepository.findById((ObjectId)httpSession.getAttribute("userId"));
        if (user == null) return new ResponseEntity<>(new NotFoundError(),HttpStatus.NOT_FOUND);
        if (userRepository.findById(
                (ObjectId) httpSession.getAttribute("userId"))
                .getProblemListLiked()
                .indexOf(problemListId)!=-1){
            return new ResponseEntity<>(new FieldsDuplicateError(),HttpStatus.BAD_REQUEST);
        }
        user.addProblemListLiked(problemListId);
        return new ResponseEntity<>(new User(),HttpStatus.OK);
    }

    //取消收藏问题
    @RequestMapping(value = "/{username}/liked-problems/{problemId}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> unlikeProblem(@PathVariable String username, @PathVariable ObjectId problemId, HttpSession httpSession) {
        if (!isSignedIn(httpSession))
            return new ResponseEntity<>(new ForbiddenError(),HttpStatus.FORBIDDEN);
        User user = userRepository.
                findById((ObjectId)httpSession.getAttribute("userId"));
        if (user == null) return new ResponseEntity<>(new NotFoundError(),HttpStatus.NOT_FOUND);
        if (userRepository
                .findById((ObjectId) httpSession.getAttribute("userId"))
                .getProblemLiked()
                .indexOf(problemId)==-1){
            return new ResponseEntity<>(new NotFoundError(),HttpStatus.NOT_FOUND);
        }
        user.deleteProblemLiked(problemId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //取消收藏题单
    @RequestMapping(value = "/{username}/liked-problem-lists/{problemListId}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> unlikeProblemLists(@PathVariable String username, @PathVariable ObjectId problemListId, HttpSession httpSession) {
        if (!isSignedIn(httpSession))
            return new ResponseEntity<>(new ForbiddenError(),HttpStatus.FORBIDDEN);
        User user = userRepository.
                findById((ObjectId)httpSession.getAttribute("userId"));
        if (user == null) return new ResponseEntity<>(new NotFoundError(),HttpStatus.NOT_FOUND);
        if (userRepository
                .findById((ObjectId) httpSession.getAttribute("userId"))
                .getProblemListLiked()
                .indexOf(problemListId)==-1){
            return new ResponseEntity<>(new NotFoundError(),HttpStatus.NOT_FOUND);
        }
        user.deleteProblemListLiked(problemListId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
