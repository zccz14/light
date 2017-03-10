package com.funcxy.oj.controllers;

import com.funcxy.oj.errors.BadRequestError;
import com.funcxy.oj.errors.ForbiddenError;
import com.funcxy.oj.errors.UnsupportedMediaType;
import com.funcxy.oj.models.ProblemList;
import com.funcxy.oj.models.User;
import com.funcxy.oj.repositories.ProblemListRepository;
import com.funcxy.oj.repositories.UserRepository;
import com.funcxy.oj.utils.DataPageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Date;

import static com.funcxy.oj.utils.UploadFiles.upload;
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

    @RequestMapping(value = "/owned", method = RequestMethod.GET)
    public ResponseEntity getProblemListsOwned(@RequestParam int pageNumber,
                                               @RequestParam int pageSize,
                                               HttpSession session) {
        if (!isSignedIn(session)) {
            return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
        }

        pageable.setPageSize(pageSize);
        pageable.setPageNumber(pageNumber);
        // 后端题单检索功能，版权所有，请勿删除。
//        if (creator != null && title != null) {
//            return new ResponseEntity(problemListRepository.findByCreatorLikeAndTitleLike(creator, title, pageable), HttpStatus.OK);
//        } else if (creator != null) {
//            return new ResponseEntity(problemListRepository.findByCreatorLike(creator, pageable), HttpStatus.OK);
//        } else if (title != null) {
//            return new ResponseEntity(problemListRepository.findByTitleLike(title, pageable), HttpStatus.OK);
//        } else {
//            return new ResponseEntity(problemListRepository.findAll(pageable), HttpStatus.OK);
//        }
        return new ResponseEntity<>(problemListRepository
                .getAllProblemListsCreated(
                        session.getAttribute("userId")
                                .toString(), pageable), HttpStatus.OK);
    }

    @RequestMapping(value = "/in", method = RequestMethod.GET)
    public ResponseEntity getProblemLists(@RequestParam int pageNumber,
                                          @RequestParam int pageSize,
                                          HttpSession session) {
        if (!isSignedIn(session)) {
            return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
        }
        pageable.setPageSize(pageSize);
        pageable.setPageNumber(pageNumber);

        return new ResponseEntity<>(problemListRepository
                .findByUserListLike(
                        session.getAttribute("userId")
                                .toString(), pageable), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity getOneSpecificProblemList(@PathVariable String id,
                                                    HttpSession session) {
        if (!isSignedIn(session)) {
            return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
        }

        ProblemList tempProblemList = problemListRepository.findById(id);

        if (tempProblemList == null) {
            return new ResponseEntity<>(new BadRequestError(), HttpStatus.BAD_REQUEST);
        }

        String tempObjectId = session.getAttribute("userId").toString();

        if (tempProblemList.getCreator().equals(tempObjectId)) {
            return new ResponseEntity<>(tempProblemList, HttpStatus.OK);
        }

        if (tempProblemList.isAccessible() ||
                tempProblemList
                        .getUserList()
                        .contains(tempObjectId)) {
            Date readBeginTime = tempProblemList.getReadBeginTime();
            Date readEndTime = tempProblemList.getReadEndTime();

            Date now = new Date(System.currentTimeMillis());

            if ((readBeginTime == null && readEndTime == null)) {
                return new ResponseEntity<>(tempProblemList, HttpStatus.OK);
            }

            if (readBeginTime != null) {
                if (readBeginTime.before(now) && readEndTime == null) {
                    return new ResponseEntity<>(tempProblemList, HttpStatus.OK);
                }
            }

            if (readEndTime != null) {
                if (readEndTime.after(now) && readBeginTime == null) {
                    return new ResponseEntity<>(tempProblemList, HttpStatus.OK);
                }
            }

            if (readBeginTime.before(now) && readEndTime.after(now)) {
                return new ResponseEntity<>(tempProblemList, HttpStatus.OK);
            }
            return new ResponseEntity<>(new BadRequestError(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity createProblemList(@Valid @RequestBody ProblemList problemList,
                                            HttpSession session) {
        if (!isSignedIn(session)) {
            return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
        }

        if (!problemList.isAccessible()) {
            problemList.setUserList(null);
        }

        String tempObjectId = session.getAttribute("userId").toString();

        problemList.setCreator(tempObjectId);
        problemList.setCreatedTime(new Date());
        ProblemList tempProblemList = problemListRepository.save(problemList);

        User user = userRepository.findById(tempObjectId);
        user.addProblemListOwned(tempProblemList.getId());
        userRepository.save(user);

        return new ResponseEntity<>(tempProblemList, HttpStatus.OK);
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public ResponseEntity createCoverTest(@RequestBody MultipartFile cover,
                                          HttpSession session) {
        if (isSignedIn(session)) {
            return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
        }

        if (cover.getOriginalFilename().matches("^\\S*.(jpg$)|(png$)|(bmp$)")) {
            return new ResponseEntity<>(upload(cover, ProblemList.PATH), HttpStatus.OK);
        }

        return new ResponseEntity<>(new UnsupportedMediaType(), HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity modifyProblemList(@RequestBody @Valid ProblemList problemList,
                                            @PathVariable String id,
                                            HttpSession session) {
        String tempObjectId = session.getAttribute("userId").toString();
        ProblemList tempProblemList = problemListRepository.findById(id);

        if (!(isSignedIn(session)
                && tempObjectId.equals(tempProblemList.getCreator()))) {
            return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
        }

        if (problemList.isAccessible()) {
            problemList.setUserList(null);
        }

        problemList.setId(id);
        problemList.setCreator(tempObjectId);

        return new ResponseEntity<>(problemListRepository.save(problemList), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteProblemList(@PathVariable String id,
                                            HttpSession session) {
        ProblemList tempProblemList = problemListRepository.findById(id);

        String tempObjectId = session.getAttribute("userId").toString();

        if (!(isSignedIn(session)
                && tempObjectId
                .equals(tempProblemList
                        .getCreator()))) {
            return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
        }

        problemListRepository.delete(tempProblemList);

        User user = userRepository.findById(tempObjectId);
        user.deleteProblemListOwned(tempProblemList.getId());
        userRepository.save(user);

        return new ResponseEntity<>(tempProblemList, HttpStatus.OK);
    }
}