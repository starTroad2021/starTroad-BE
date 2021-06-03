package com.BE.starTroad.controller;

import com.BE.starTroad.config.JwtTokenUtil;
import com.BE.starTroad.domain.Request;
import com.BE.starTroad.domain.Studier;
import com.BE.starTroad.domain.Study;
import com.BE.starTroad.service.JpaRequestService;
import com.BE.starTroad.service.JpaStudierService;
import com.BE.starTroad.service.JpaStudyService;
import com.BE.starTroad.service.JpaUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api/study")
public class StudyController {

    @Autowired
    JpaStudyService jpaStudyService;

    @Autowired
    JpaStudierService jpaStudierService;

    @Autowired
    JpaRequestService jpaRequestService;

    @Autowired
    JpaUserService jpaUserService;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    //스터디 리스트 가져오기
    @GetMapping(value="/{roadmap_id}")
    public ResponseEntity<List<StudyForm>> studyList(@PathVariable("roadmap_id") Long id, @RequestHeader("Authorization") String token) {

        token = token.substring(7);
        String tokenOwner = jwtTokenUtil.getUsernameFromToken(token);

        int mapId = id.intValue();
        List <Study> studyList = jpaStudyService.findByFollowMap(mapId);
        String leader = "";
        int study_id;
        List<StudyForm> studyFormList = new ArrayList<>();
        int listSize = studyList.size();
        for (int i=0;i<listSize;i++) {
            StudyForm studyForm = new StudyForm();
            study_id = studyList.get(i).getId();
            studyForm.setId(study_id);
            studyForm.setName(studyList.get(i).getName());
            studyForm.setFollow_map(studyList.get(i).getFollowMap());
            leader = studyList.get(i).getLeader();
            studyForm.setLeader(leader);
            studyForm.setCreated_at(studyList.get(i).getCreatedAt().toString());
            studyForm.setDescription(studyList.get(i).getDescription());
            studyForm.setMax_num(studyList.get(i).getMaxNum());
            studyForm.setStatus(studyList.get(i).getStatus());
            //now_num
            int num = jpaStudierService.studyNum(study_id);
            studyForm.setNow_num(num);
            studyFormList.add(studyForm);
        }

        return new ResponseEntity<List<StudyForm>>(studyFormList, HttpStatus.OK);
    }
    //스터디 생성하기
    @PostMapping(value="{roadmap_id}")
    public ResponseEntity<Study> generateStudy(@PathVariable int roadmap_id, StudyForm study, @RequestHeader("Authorization") String token) {

        token = token.substring(7);
        String tokenOwner = jwtTokenUtil.getUsernameFromToken(token);

        String timestamp = study.getCreated_at();
        Timestamp time;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
            Date parsedDate = dateFormat.parse(timestamp);
            time = new Timestamp(parsedDate.getTime());
        } catch(Exception e) {
            time = new Timestamp(System.currentTimeMillis());
        }

        Study newStudy = new Study();
        Study studyId = new Study();

        newStudy.setName(study.getName());
        newStudy.setFollowMap(study.getFollow_map());
        newStudy.setLeader(tokenOwner);
        newStudy.setCreatedAt(time);
        newStudy.setDescription(study.getDescription());
        newStudy.setMaxNum(study.getMax_num());
        newStudy.setStatus(study.getStatus());
        studyId = jpaStudyService.save(newStudy);

        Studier studier = new Studier();
        studier.setStudyId(studyId.getId());
        studier.setEmail(tokenOwner);
        jpaStudierService.save(studier);


        return new ResponseEntity<Study>(newStudy, HttpStatus.OK);
    }
    //스터디 세부사항 조회
    @GetMapping(value="/{roadmap_id}/{study_id}")
    public ResponseEntity<StudyForm> detailStudy(@PathVariable int roadmap_id, @PathVariable int study_id,
                                                 @RequestHeader ("Authorization") String token) {
        token = token.substring(7);
        String tokenOwner = jwtTokenUtil.getUsernameFromToken(token);

        StudyForm studyForm = new StudyForm();
        Optional<Study> dbStudy = jpaStudyService.findById(study_id);
        if (dbStudy.isPresent()) {
            studyForm.setId(dbStudy.get().getId());
            studyForm.setName(dbStudy.get().getName());
            studyForm.setFollow_map(dbStudy.get().getFollowMap());
            studyForm.setLeader(dbStudy.get().getLeader());
            studyForm.setCreated_at(dbStudy.get().getCreatedAt().toString());
            studyForm.setDescription(dbStudy.get().getDescription());
            studyForm.setMax_num(dbStudy.get().getMaxNum());
            studyForm.setStatus(dbStudy.get().getStatus());

            //현재 참여 인원
            int num = jpaStudierService.studyNum(study_id);
            studyForm.setNow_num(num);

            if (dbStudy.get().getLeader().equals(tokenOwner)) {
                studyForm.setValid("yes");
                studyForm.setJoinValid("yes");
            }
            else {
                studyForm.setValid("no");
            }

            Optional<Studier> amI = jpaStudierService.findAmIJoin(study_id, tokenOwner);

            if (amI.isPresent()) {
                studyForm.setJoinValid("yes");
            }
            else {
                studyForm.setJoinValid("no");
            }

            //request table에 있으면 pending
            Optional<Request> request = jpaRequestService.findByStudyIdAndRequester(study_id, tokenOwner);

            if (request.isPresent()) {
                studyForm.setJoinValid("pending");
            }

            return new ResponseEntity<StudyForm>(studyForm, HttpStatus.OK);
        }

        else {
           return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
    //나의 스터디
    @GetMapping(value="/mystudy")
    public ResponseEntity<List<StudyForm>> myStudy(@RequestHeader ("Authorization") String token) {

        token = token.substring(7);
        String tokenOwner = jwtTokenUtil.getUsernameFromToken(token);

        List<StudyForm> myStudies = new ArrayList<>();
        List<Studier> myStudiers = jpaStudierService.findJoinStudy(tokenOwner);
        Study study = new Study();
        int listSize = myStudiers.size();
        int studyId;
        for (int i=0;i<listSize;i++) {
            studyId = myStudiers.get(i).getStudyId();
            study = jpaStudyService.findById(studyId).get();
            if (study != null) {
		
                StudyForm studyForm = new StudyForm();
                studyForm.setId(studyId);
                studyForm.setName(study.getName());
                studyForm.setFollow_map(study.getFollowMap());
                studyForm.setLeader(study.getLeader());
                studyForm.setCreated_at(study.getCreatedAt().toString());
                studyForm.setDescription(study.getDescription());
                studyForm.setMax_num(study.getMaxNum());
                studyForm.setStatus(study.getStatus());

                if (study.getLeader().equals(tokenOwner)) {
                    studyForm.setValid("yes");
                    studyForm.setJoinValid("yes");
                }
                else {
                    studyForm.setValid("no");
                    studyForm.setJoinValid("yes");
                }
                //now num
                int num = jpaStudierService.studyNum(studyId);
                studyForm.setNow_num(num);
                myStudies.add(studyForm);
            }
        }
        return new ResponseEntity<List<StudyForm>>(myStudies, HttpStatus.OK);
    }
    //스터디 참여 요청 리스트
    @GetMapping(value = "/requestlist")
    public ResponseEntity<List<RequestForm>> requestList(@RequestHeader ("Authorization") String token) {
        token = token.substring(7);
        String tokenOwner = jwtTokenUtil.getUsernameFromToken(token);

        List<Request> myRequests = jpaRequestService.findByHead(tokenOwner);
        String name = "";
        int study_id;
        String study_name = "";
        String requester = "";
        List<RequestForm> reqForm = new ArrayList<>();
        int listSize = myRequests.size();
        for (int i=0;i<listSize;i++) {
            RequestForm req = new RequestForm();
            req.setId(myRequests.get(i).getId());
            study_id = myRequests.get(i).getStudyId();
            req.setStudy_id(study_id);
            study_name = jpaStudyService.findById(study_id).get().getName();
            req.setStudy_name(study_name);
            req.setHead(myRequests.get(i).getHead());
            requester = myRequests.get(i).getRequester();
            req.setRequester(requester);
            name = jpaUserService.findByEmail(requester).get().getName();
            req.setRequesterName(name);
            reqForm.add(req);
        }
        return new ResponseEntity<List<RequestForm>>(reqForm, HttpStatus.OK);
    }
    //스터디 참여요청
    @PostMapping(value = "/{roadmap_id}/{study_id}/ask")
    public ResponseEntity<Request> studyRequest(@PathVariable Long roadmap_id, @PathVariable int study_id, @RequestHeader ("Authorization") String token) {
        token = token.substring(7);
        String tokenOwner = jwtTokenUtil.getUsernameFromToken(token);

        String studyHead;
        Request newRequest = new Request();
        Optional<Study> dbStudy = jpaStudyService.findById(study_id);
        if (dbStudy.isPresent()) {
            studyHead = dbStudy.get().getLeader();
            newRequest.setHead(studyHead);
            newRequest.setRequester(tokenOwner);
            newRequest.setStudyId(study_id);
            jpaRequestService.save(newRequest);
        }
        else {
            return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<Request>(newRequest, HttpStatus.OK);
    }

    //스터디 참여요청 수락
    @PostMapping(value = "/{roadmap_id}/{study_id}/accept")
    public ResponseEntity<Request> acceptRequest(@PathVariable Long roadmap_id, @PathVariable int study_id,
                                                 @RequestHeader ("Authorization") String token,
                                                 String requester) {
        token = token.substring(7);
        String tokenOwner = jwtTokenUtil.getUsernameFromToken(token);

        Optional<Request> request = jpaRequestService.findByStudyIdAndRequester(study_id, requester);

        if (request.isPresent()) {
            int requestId = request.get().getId();
            jpaRequestService.accept(requestId);
        }
        else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<Request>(request.get(), HttpStatus.OK);
    }

    //스터디 참여요청 거절
    @PostMapping(value = "/{roadmap_id}/{study_id}/deny")
    public ResponseEntity<Request> denyRequest(@PathVariable Long roadmap_id, @PathVariable int study_id,
                                                 @RequestHeader ("Authorization") String token,
                                                 String requester) {
        token = token.substring(7);
        String tokenOwner = jwtTokenUtil.getUsernameFromToken(token);

        Optional<Request> request = jpaRequestService.findByStudyIdAndRequester(study_id, requester);

        if (request.isPresent()) {
            int requestId = request.get().getId();
            jpaRequestService.accept(requestId);
        }
        else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<Request>(request.get(), HttpStatus.OK);
    }

    //스터디 탈퇴
    @PostMapping(value = "/{roadmap_id}/{study_id}/quit")
    public ResponseEntity<Studier> quitStudy(@PathVariable Long roadmap_id, @PathVariable int study_id,
                                      @RequestHeader ("Authorization") String token) {
        token = token.substring(7);
        String tokenOwner = jwtTokenUtil.getUsernameFromToken(token);

        Studier studier = jpaStudierService.quit(study_id, tokenOwner);

        if (studier != null) {
            return new ResponseEntity<Studier>(studier, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    //스터디 내용 수정
    @PutMapping(value="/modify/{roadmap_id}/{study_id}")
    public ResponseEntity<Study> modifyStudy(@PathVariable int roadmap_id, @PathVariable int study_id,
                                             StudyForm study, @RequestHeader("Authorization") String token) {

        token = token.substring(7);
        String tokenOwner = jwtTokenUtil.getUsernameFromToken(token);
        Optional<Study> dbStudy = jpaStudyService.findById(study_id);
        String studyOwner = "";

        if (dbStudy.isPresent()) {
            studyOwner = dbStudy.get().getLeader();
        }
        if (!(tokenOwner.equals(studyOwner))) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }

        String timestamp = study.getCreated_at();
        Timestamp time;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
            Date parsedDate = dateFormat.parse(timestamp);
            time = new Timestamp(parsedDate.getTime());
        } catch(Exception e) {
            time = new Timestamp(System.currentTimeMillis());
        }

        Study newStudy = new Study();
	newStudy.setId(study_id);
        newStudy.setName(study.getName());
        newStudy.setFollowMap(study.getFollow_map());
        newStudy.setLeader(tokenOwner);
        newStudy.setCreatedAt(time);
        newStudy.setDescription(study.getDescription());
        newStudy.setMaxNum(study.getMax_num());
        newStudy.setStatus(study.getStatus());

        Study std = jpaStudyService.update(study_id, newStudy);

        if (std != null) {
            return new ResponseEntity<Study>(std, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
        }
    }


}
