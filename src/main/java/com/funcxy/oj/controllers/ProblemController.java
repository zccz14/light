package com.funcxy.oj.controllers;

import com.funcxy.oj.errors.BadRequestError;
import com.funcxy.oj.errors.ForbiddenError;
import com.funcxy.oj.errors.NotFoundError;
import com.funcxy.oj.models.Problem;
import com.funcxy.oj.models.User;
import com.funcxy.oj.repositories.ProblemRepository;
import com.funcxy.oj.repositories.UserRepository;
import com.funcxy.oj.utils.DataPageable;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import static com.funcxy.oj.utils.UserUtil.isSignedIn;

/**
 * Created by wtupc96 on 2017/2/28.
 *
 * @author Peter
 * @version 1.0
 */

@RestController
@RequestMapping("/problems")
public class ProblemController {
    private static final Sort sort = new Sort(Sort.Direction.ASC, "title");

    @Autowired
    ProblemRepository problemRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    MongoTemplate mongoTemplate;
    private DataPageable pageable;

    {
        pageable = new DataPageable();
        pageable.setSort(sort);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity saveProblem(@RequestBody Problem problem, HttpSession session) {
        if (!isSignedIn(session)) {
            return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
        }

        problem.setCreator(new ObjectId(session.getAttribute("userId").toString()));

        Problem tempProblem = problemRepository.save(problem);
        User user = userRepository.findById(new ObjectId(session.getAttribute("userId").toString()));

        user.addProblemOwned(tempProblem.getId());
        userRepository.save(user);
        return new ResponseEntity<>(tempProblem, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity getOneSpecificProblem(@PathVariable ObjectId id, HttpSession session) {
        if (!isSignedIn(session)) {
            return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
        }

        Problem tempProblem = problemRepository.findById(id);

        if (tempProblem == null) {
            return new ResponseEntity<>(new BadRequestError(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(tempProblem, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity getProblem(@RequestParam int pageNumber, @RequestParam int pageSize, HttpSession session) {
        if (!isSignedIn(session)) {
            return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
        }

        pageable.setPageNumber(pageNumber);
        pageable.setPageSize(pageSize);

        //后端检索问题功能，请勿删除。
//        String type = problem.getType();
//        String title = problem.getTitle();
//        User creator = problem.getCreator();

//        if (type != null && title != null && creator != null) {
//            return new ResponseEntity<>(problemRepository.findByTypeLikeAndTitleLikeAndCreatorLike(type, title, creator, pageable), HttpStatus.OK);
//        } else if (type != null && title != null) {
//            return new ResponseEntity<>(problemRepository.findByTypeLikeAndTitleLike(type, title, pageable), HttpStatus.OK);
//        } else if (type != null && creator != null) {
//            return new ResponseEntity<>(problemRepository.findByTypeLikeAndCreatorLike(type, creator, pageable), HttpStatus.OK);
//        } else if (title != null && creator != null) {
//            return new ResponseEntity<>(problemRepository.findByCreatorLikeAndTitleLike(creator, title, pageable), HttpStatus.OK);
//        } else if (type != null) {
//            return new ResponseEntity<>(problemRepository.findByTypeLike(type, pageable), HttpStatus.OK);
//        } else if (title != null) {
//            return new ResponseEntity<>(problemRepository.findByTitleLike(title, pageable), HttpStatus.OK);
//        } else if (creator != null) {
//            return new ResponseEntity<>(problemRepository.findByCreatorLike(creator, pageable), HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>(problemRepository.findAll(pageable), HttpStatus.OK);
//        }
        return new ResponseEntity(problemRepository
                .getAllProblems(new ObjectId(session
                        .getAttribute("userId")
                        .toString()), pageable), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity updateProblem(@RequestBody @Valid Problem problem, @PathVariable ObjectId id, HttpSession session) {
        if (!(isSignedIn(session) &&
                new ObjectId(session
                        .getAttribute("userId")
                        .toString())
                        .equals(problem
                                .getCreator()))) {
            return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
        }
        problem.setId(id);
        return new ResponseEntity<Object>(problemRepository.save(problem), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteProblem(@PathVariable ObjectId id, HttpSession session) {
        if (!(isSignedIn(session) &&
                new ObjectId(session
                        .getAttribute("userId")
                        .toString())
                        .equals(problemRepository
                                .findById(id)
                                .getCreator()))) {
            return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
        }
        Problem tempProblem = problemRepository.findById(id);
        if (tempProblem == null) return new ResponseEntity<>(new NotFoundError(), HttpStatus.NOT_FOUND);
        problemRepository.delete(tempProblem);

        User user = userRepository.findById(new ObjectId(session.getAttribute("userId").toString()));
        user.deleteProblemOwned(tempProblem.getId());
        userRepository.save(user);
        return new ResponseEntity<>(tempProblem, HttpStatus.OK);
    }
}
