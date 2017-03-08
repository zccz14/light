package com.funcxy.oj.controllers;

import com.funcxy.oj.errors.BadRequestError;
import com.funcxy.oj.errors.ForbiddenError;
import com.funcxy.oj.models.CleanedProblem;
import com.funcxy.oj.models.Problem;
import com.funcxy.oj.repositories.ProblemRepository;
import com.funcxy.oj.repositories.UserRepository;
import com.funcxy.oj.utils.DataPageable;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

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
    public ResponseEntity saveProblem(@Valid Problem problem, HttpSession session) {
        if (!isSignedIn(session)) {
            return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
        }

        Problem tempProblem = problemRepository.save(problem);

        userRepository
                .findById((ObjectId) session.getAttribute("userId"))
                .addProblemOwned(tempProblem.getId());

        return new ResponseEntity<>(tempProblem, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity getOneSpecificProblem(@PathVariable ObjectId id, HttpSession session) {
        if (!isSignedIn(session)) {
            return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
        }

        Problem tempProblem = problemRepository.findById(id);

        if (tempProblem == null) {
            return new ResponseEntity(new BadRequestError(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(tempProblem, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity getProblem(Problem problem, @RequestParam int pageNumber, @RequestParam int pageSize, HttpSession session) {
//        if (!isSignedIn(session)) {
//            return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
//        }

        pageable.setPageNumber(pageNumber);
        pageable.setPageSize(pageSize);

        List<Problem> problemIdList = null;
        if (problem.getType() != null) {
//            problemIdList = problemRepository.findByTheArg("type", problem.getType());
            problemIdList = problemRepository.findByTypeLike(problem.getType(), pageable);
        }
        if (problem.getTitle() != null) {
            if (problemIdList == null) {
//                problemIdList = problemRepository.findByTheArg("title", problem.getTitle());
                problemIdList = problemRepository.findByTitleLike(problem.getTitle(), pageable);
            } else {
//                problemIdList.retainAll(problemRepository.findByTheArg("title", problem.getTitle()));
                problemIdList.retainAll(problemRepository.findByTitleLike(problem.getTitle(), pageable));
            }
        }
        if (problem.getCreator() != null) {
            if (problemIdList == null) {
//                problemIdList = problemRepository.findByTheArg("creator", problem.getCreator());
                problemIdList = problemRepository.findByCreatorLike(problem.getCreator(), pageable);
            } else {
//                problemIdList.retainAll(problemRepository.findByTheArg("creator", problem.getCreator()));
                problemIdList.retainAll(problemRepository.findByCreatorLike(problem.getCreator(), pageable));
            }
        }

        if (problemIdList == null) {
            return new ResponseEntity(problemRepository.findAll(pageable), HttpStatus.OK);
        }

        return
                new ResponseEntity<>
                        (new PageImpl<CleanedProblem>(problemIdList.stream()
                                .map(pro
                                        -> new CleanedProblem(pro.getId(), pro.getTitle()))
                                .collect(Collectors.toList()), pageable, problemIdList.size())
                                , HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity updateProblem(@RequestBody @Valid Problem problem, @PathVariable ObjectId id, HttpSession session) {
        if (!isSignedIn(session)) {
            return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
        }
//        Problem tempProblem = problemRepository.findById(id);
//        if (problem.getReferenceAnswer() == null) {
//            problem.setReferenceAnswer(tempProblem.getReferenceAnswer());
//        }
        problem.setId(id);
        return new ResponseEntity<Object>(problemRepository.save(problem), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteProblem(@PathVariable ObjectId id, HttpSession session) {
        if (!isSignedIn(session)) {
            return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
        }
        Problem tempProblem = problemRepository.findById(id);
        problemRepository.delete(tempProblem);

        userRepository
                .findById((ObjectId) session.getAttribute("userId"))
                .deleteProblemOwned(tempProblem.getId());

        return new ResponseEntity<>(tempProblem, HttpStatus.OK);
    }
}
