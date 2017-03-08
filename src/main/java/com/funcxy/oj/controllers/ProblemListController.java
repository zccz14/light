package com.funcxy.oj.controllers;

import com.funcxy.oj.errors.BadRequestError;
import com.funcxy.oj.errors.ForbiddenError;
import com.funcxy.oj.models.ProblemList;
import com.funcxy.oj.models.User;
import com.funcxy.oj.repositories.ProblemListRepository;
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
 * Created by wtupc96 on 2017/3/4.
 *
 * @author Peter
 * @version 1.0
 */

@RestController
@RequestMapping("/problemLists")
public class ProblemListController {
    private static final Sort sort = new Sort(Sort.Direction.ASC, "title");

    @Autowired
    private ProblemListRepository problemListRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private UserRepository userRepository;

    private DataPageable pageable;

    {
        pageable = new DataPageable();
        pageable.setSort(sort);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity getProblemLists(@RequestParam int pageNumber, @RequestParam int pageSize, @RequestParam User creator, @RequestParam String title, HttpSession session) {
        if (!isSignedIn(session)) {
            return new ResponseEntity(new ForbiddenError(), HttpStatus.FORBIDDEN);
        }

        pageable.setPageSize(pageSize);
        pageable.setPageNumber(pageNumber);

        if (creator != null && title != null) {
            return new ResponseEntity(problemListRepository.findByCreatorLikeAndTitleLike(creator, title, pageable), HttpStatus.OK);
        } else if (creator != null) {
            return new ResponseEntity(problemListRepository.findByCreatorLike(creator, pageable), HttpStatus.OK);
        } else if (title != null) {
            return new ResponseEntity(problemListRepository.findByTitleLike(title, pageable), HttpStatus.OK);
        } else {
            return new ResponseEntity(problemListRepository.findAll(pageable), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity getOneSpecificProblemList(@PathVariable ObjectId id, HttpSession session) {
        if (!isSignedIn(session)) {
            return new ResponseEntity(new ForbiddenError(), HttpStatus.FORBIDDEN);
        }

        ProblemList tempProblemList = problemListRepository.findById(id);

        if (tempProblemList == null) {
            return new ResponseEntity(new BadRequestError(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(tempProblemList, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity createProblemList(@Valid ProblemList problemList, HttpSession session) {
        if (!isSignedIn(session)) {
            return new ResponseEntity(new ForbiddenError(), HttpStatus.FORBIDDEN);
        }

        ProblemList tempProblemList = problemListRepository.save(problemList);

        userRepository
                .findById(new ObjectId(session.getAttribute("userId").toString()))
                .addProblemListOwned(tempProblemList.getId());

        return new ResponseEntity(tempProblemList, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity modifyProblemList(@RequestBody @Valid ProblemList problemList, @PathVariable ObjectId id, HttpSession session) {
        if (!isSignedIn(session)) {
            return new ResponseEntity(new ForbiddenError(), HttpStatus.FORBIDDEN);
        }
        problemList.setId(id);
        return new ResponseEntity(problemListRepository.save(problemList), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteProblemList(@PathVariable ObjectId id, HttpSession session) {
        if (!isSignedIn(session)) {
            return new ResponseEntity(new ForbiddenError(), HttpStatus.FORBIDDEN);
        }
        ProblemList tempProblemList = problemListRepository.findById(id);
        problemListRepository.delete(tempProblemList);

        userRepository
                .findById(new ObjectId(session.getAttribute("userId").toString()))
                .deleteProblemListOwned(tempProblemList.getId());

        return new ResponseEntity(tempProblemList, HttpStatus.OK);
    }
}
