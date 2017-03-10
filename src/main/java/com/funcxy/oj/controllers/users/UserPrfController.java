package com.funcxy.oj.controllers.users;

import com.funcxy.oj.errors.FieldsDuplicateError;
import com.funcxy.oj.errors.ForbiddenError;
import com.funcxy.oj.errors.NotFoundError;
import com.funcxy.oj.models.CleanedProblem;
import com.funcxy.oj.models.CleanedProblemList;
import com.funcxy.oj.models.User;
import com.funcxy.oj.repositories.ProblemListRepository;
import com.funcxy.oj.repositories.ProblemRepository;
import com.funcxy.oj.repositories.UserRepository;
import com.funcxy.oj.utils.DataPageable;
import com.funcxy.oj.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
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
 * @author  DDHEE on 2017/3/7.
 */

@RestController
@RequestMapping("/users")
public class UserPrfController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    ProblemRepository problemRepository;
    @Autowired
    ProblemListRepository problemListRepository;
    private DataPageable pageable;

    {
        pageable = new DataPageable();
        pageable.setSort(new Sort(Sort.Direction.ASC, "title"));
    }
    //收藏问题
    @RequestMapping(value = "/{username}/liked-problems/{problemId}", method = RequestMethod.POST)
    public ResponseEntity<Object> likeProblem(@PathVariable String username, @PathVariable String problemId, HttpSession httpSession) {
        if (!isSignedIn(httpSession))
            return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
        User user = userRepository.findById(httpSession.getAttribute("userId").toString());
        if (user == null) return new ResponseEntity<>(new NotFoundError(), HttpStatus.NOT_FOUND);
        if (userRepository.findById(
                httpSession.getAttribute("userId").toString())
                .getProblemLiked()
                .indexOf(problemId) != -1) {
            return new ResponseEntity<>(new FieldsDuplicateError(), HttpStatus.BAD_REQUEST);
        }
        user.addProblemLiked(problemId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //收藏题单
    @RequestMapping(value = "/{username}/liked-problem-lists/{problemListId}", method = RequestMethod.POST)
    public ResponseEntity<Object> likeProblemList(@PathVariable String username, @PathVariable String problemListId, HttpSession httpSession) {
        if (!isSignedIn(httpSession))
            return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);

        User user = userRepository.findById(httpSession.getAttribute("userId").toString());
        if (user == null) return new ResponseEntity<>(new NotFoundError(), HttpStatus.NOT_FOUND);
        if (userRepository.findById(
                httpSession.getAttribute("userId").toString())
                .getProblemListLiked()
                .indexOf(problemListId) != -1) {
            return new ResponseEntity<>(new FieldsDuplicateError(), HttpStatus.BAD_REQUEST);
        }
        user.addProblemListLiked(problemListId);
        return new ResponseEntity<>(new User(), HttpStatus.OK);
    }

    //取消收藏问题
    @RequestMapping(value = "/{username}/liked-problems/{problemId}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> unlikeProblem(@PathVariable String username, @PathVariable String problemId, HttpSession httpSession) {
        if (!isSignedIn(httpSession))
            return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
        User user = userRepository.
                findById(httpSession.getAttribute("userId").toString());
        if (user == null) return new ResponseEntity<>(new NotFoundError(), HttpStatus.NOT_FOUND);
        if (userRepository
                .findById(httpSession.getAttribute("userId").toString())
                .getProblemLiked()
                .indexOf(problemId) == -1) {
            return new ResponseEntity<>(new NotFoundError(), HttpStatus.NOT_FOUND);
        }
        user.deleteProblemLiked(problemId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //取消收藏题单
    @RequestMapping(value = "/{username}/liked-problem-lists/{problemListId}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> unlikeProblemLists(@PathVariable String username, @PathVariable String problemListId, HttpSession httpSession) {
        if (!isSignedIn(httpSession))
            return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
        User user = userRepository.
                findById(httpSession.getAttribute("userId").toString());
        if (user == null) return new ResponseEntity<>(new NotFoundError(), HttpStatus.NOT_FOUND);
        if (userRepository
                .findById(httpSession.getAttribute("userId").toString())
                .getProblemListLiked()
                .indexOf(problemListId) == -1) {
            return new ResponseEntity<>(new NotFoundError(), HttpStatus.NOT_FOUND);
        }
        user.deleteProblemListLiked(problemListId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    //获取收藏的题单
    @RequestMapping(value = "/{username}/liked-problem-lists", method = RequestMethod.GET)
    public ResponseEntity getLikedProblemLists(@PathVariable String username, HttpSession httpSession) {
        if (UserUtil.isSignedIn(httpSession)) {
            User user = userRepository.findById(httpSession.getAttribute("userId").toString());
            if (user == null) return new ResponseEntity<>(new NotFoundError(), HttpStatus.NOT_FOUND);
            List<String> likedProblemList = user.getProblemListLiked();
            return new ResponseEntity<>(new PageImpl<CleanedProblemList>(
                    likedProblemList
                            .stream()
                            .map(
                                    pro -> new CleanedProblemList(
                                            problemListRepository.findById(pro).getId(),
                                            problemListRepository.findById(pro).getTitle(),
                                            problemListRepository.findById(pro).getType()
                                    )
                            )
                            .collect(Collectors.toList())
                    , pageable
                    , likedProblemList.size()
            )
                    , HttpStatus.FOUND
            );
        } else {
            return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
        }
    }

    //获取收藏的题目
    @RequestMapping(value = "/{username}/liked-problems", method = RequestMethod.GET)
    public ResponseEntity getLikedProblems(@PathVariable String username, HttpSession httpSession) {
        if (!UserUtil.isSignedIn(httpSession)) {
            return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
        }
        User user = userRepository.findById(httpSession.getAttribute("userId").toString());
        if (user == null) {
            return new ResponseEntity<>(new NotFoundError(), HttpStatus.NOT_FOUND);
        }
        List<String> likedProblem = user.getProblemLiked();

        return new ResponseEntity<>(
                new PageImpl<CleanedProblem>(
                        likedProblem.stream()
                                .map(
                                        pro -> new CleanedProblem(
                                                problemRepository.findById(pro).getId(),
                                                problemRepository.findById(pro).getTitle()
                                        )
                                ).collect(Collectors.toList()),
                        pageable,
                        likedProblem.size()
                ),
                HttpStatus.OK
        );
    }
}
