package com.BE.starTroad.controller;

import com.BE.starTroad.config.JwtTokenUtil;
import com.BE.starTroad.domain.Comment;
import com.BE.starTroad.domain.Roadmap;
import com.BE.starTroad.domain.Talk;
import com.BE.starTroad.service.JpaCommentService;
import com.BE.starTroad.service.JpaTalkService;
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
@RequestMapping("/api/talk")
public class TalkController {

    @Autowired
    JpaTalkService jpaTalkService;

    @Autowired
    JpaCommentService jpaCommentService;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    //해당 로드맵의 talk 전체 조회
    @GetMapping(value="/{roadmap_id}")
    public ResponseEntity<List<Talk>> talkList(@PathVariable("roadmap_id") int mapId) {

        List<Talk> talkList = jpaTalkService.findByTalk_Roadmap(mapId);

        return new ResponseEntity<List<Talk>>(talkList, HttpStatus.OK);
    }

    //로드맵 생성
    @PostMapping(value="/{roadmap_id}")
    public ResponseEntity<Talk> generateTalke(@PathVariable int roadmapId, TalkForm talk, @RequestHeader("Authorization") String token) {

        token = token.substring(7);
        String tokenOwner = jwtTokenUtil.getUsernameFromToken(token);

        String timestamp = talk.getCreated_at();
        Timestamp time;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
            Date parsedDate = dateFormat.parse(timestamp);
            time = new Timestamp(parsedDate.getTime());
        } catch(Exception e) {
            time = new Timestamp(System.currentTimeMillis());
        }

        Talk newTalk = new Talk();

        newTalk.setName(talk.getName());
        newTalk.setCreated_at(time);
        newTalk.setTalkRoadmap(roadmapId);
        newTalk.setTalkWriter(tokenOwner);
        newTalk.setDescription(talk.getDescription());
        jpaTalkService.save(newTalk);

        return new ResponseEntity<Talk>(newTalk, HttpStatus.OK);

    }

    //한 talk 조회
    @GetMapping(value="/{roadmap_id}/{talk_id}")
    public ResponseEntity<TalkForm> detailTalk(@PathVariable int talkId, @PathVariable int mapId,
                                               @RequestHeader ("Authorization") String token) {

        token = token.substring(7);
        String tokenOwner = jwtTokenUtil.getUsernameFromToken(token);

        TalkForm talkForm = new TalkForm();
        List<CommentForm> commentForm = new ArrayList<>();
        
        Optional<Talk> dbTalk = jpaTalkService.findById(talkId);

        if (dbTalk.isPresent()) {
            talkForm.setId(dbTalk.get().getId());
            talkForm.setName(dbTalk.get().getName());
            talkForm.setCreated_at(dbTalk.get().getCreated_at().toString());
            talkForm.setTalk_roadmap(dbTalk.get().getTalkRoadmap());
            talkForm.setTalk_writer(dbTalk.get().getTalkWriter());
            talkForm.setDescription(dbTalk.get().getDescription());

            List<Comment> thisComment = jpaCommentService.findByTalk(talkId);

            for (int i =0; i < thisComment.size(); i++) {
                if (thisComment.get(i).getCommentWriter().equals(tokenOwner)) {
                    commentForm.get(i).setCommentValid("yes");
                }
                else {
                    commentForm.get(i).setCommentValid("no");
                }
                commentForm.get(i).setId(thisComment.get(i).getId());
                commentForm.get(i).setCreated_at(thisComment.get(i).getCreated_at().toString());
                commentForm.get(i).setComment_talk(thisComment.get(i).getCommentTalk());
                commentForm.get(i).setComment_writer(thisComment.get(i).getCommentWriter());
                commentForm.get(i).setContent(thisComment.get(i).getContent());
            }

            talkForm.setMyComments(commentForm);
            return new ResponseEntity<TalkForm>(talkForm, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(value="/{roadmap_id}/{talk_id}")
    public ResponseEntity<Comment> writeComment(@PathVariable int talkId, @PathVariable int mapId,
                                         CommentForm comment, @RequestHeader ("Authorization") String token) {

        token = token.substring(7);
        String tokenOwner = jwtTokenUtil.getUsernameFromToken(token);

        String timestamp = comment.getCreated_at();
        Timestamp time;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
            Date parsedDate = dateFormat.parse(timestamp);
            time = new Timestamp(parsedDate.getTime());
        } catch(Exception e) {
            time = new Timestamp(System.currentTimeMillis());
        }

        Comment newComment = new Comment();

        newComment.setCreated_at(time);
        newComment.setCommentTalk(comment.getComment_talk());
        newComment.setCommentWriter(tokenOwner);
        newComment.setContent(comment.getContent());

        return new ResponseEntity<Comment>(newComment, HttpStatus.OK);
    }

}
