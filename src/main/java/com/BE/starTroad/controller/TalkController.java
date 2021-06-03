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
@RequestMapping("/api/roadmap/talk")
public class TalkController {

    @Autowired
    JpaTalkService jpaTalkService;

    @Autowired
    JpaCommentService jpaCommentService;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    //해당 로드맵의 talk 전체 조회
    @GetMapping(value="/{roadmap_id}")
    public ResponseEntity<List<Talk>> talkList(@PathVariable int roadmap_id) {

        List<Talk> talkList = jpaTalkService.findByTalk_Roadmap(roadmap_id);

        return new ResponseEntity<List<Talk>>(talkList, HttpStatus.OK);
    }

    //talk 생성
    @PostMapping(value="/{roadmap_id}")
    public ResponseEntity<Talk> generateTalk(@PathVariable int roadmap_id, TalkForm talk, @RequestHeader("Authorization") String token) {

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
        newTalk.setTalkRoadmap(roadmap_id);
        newTalk.setTalkWriter(tokenOwner);
        newTalk.setDescription(talk.getDescription());
        jpaTalkService.save(newTalk);

        return new ResponseEntity<Talk>(newTalk, HttpStatus.OK);

    }

    //토크 수정
    @PutMapping(value="/modify/{roadmap_id}/{talk_id}")
    public ResponseEntity<Talk> modifyTalk(@PathVariable int roadmap_id, @PathVariable int talk_id,
                                       TalkForm talk, @RequestHeader ("Authorization") String token) {
        token = token.substring(7);
        String tokenOwner = jwtTokenUtil.getUsernameFromToken(token);
        String talkOwner = "";
        Long talkId = (long) talk_id;
        Optional<Talk> dbTalk = jpaTalkService.findById(talkId);

        if (dbTalk.isPresent()) {
            talkOwner = dbTalk.get().getTalkWriter();
        }
        if (!(talkOwner.equals(talkOwner))) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }

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

        newTalk.setId(talk.getId());
        newTalk.setName(talk.getName());
        newTalk.setCreated_at(time);
        newTalk.setTalkRoadmap(talk.getTalk_roadmap());
        newTalk.setTalkWriter(talk.getTalk_writer());
        newTalk.setDescription(talk.getDescription());

        Talk tk = jpaTalkService.update(talkId, newTalk);

        if (tk != null) {
            return new ResponseEntity<Talk>(tk, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

    }

    //토크 삭제

    //한 talk 조회
    @GetMapping(value="/{roadmap_id}/{talk_id}")
    public ResponseEntity<TalkForm> detailTalk(@PathVariable int roadmap_id, @PathVariable int talk_id,
                                               @RequestHeader ("Authorization") String token) {

        token = token.substring(7);
        String tokenOwner = jwtTokenUtil.getUsernameFromToken(token);

        TalkForm talkForm = new TalkForm();
        CommentForm commentForm = new CommentForm();

        List<CommentForm> comments = new ArrayList<>();
        Long talkId = (long) talk_id;
        Optional<Talk> dbTalk = jpaTalkService.findById(talkId);

        if (dbTalk.isPresent()) {
            talkForm.setId(dbTalk.get().getId());
            talkForm.setName(dbTalk.get().getName());
            talkForm.setCreated_at(dbTalk.get().getCreated_at().toString());
            talkForm.setTalk_roadmap(dbTalk.get().getTalkRoadmap());
            talkForm.setTalk_writer(dbTalk.get().getTalkWriter());
            talkForm.setDescription(dbTalk.get().getDescription());

            if (dbTalk.get().getTalkWriter().equals(tokenOwner)) {
                talkForm.setTalkValid("yes");
            }
            else {
                talkForm.setTalkValid("no");
            }

            List<Comment> thisComment = jpaCommentService.findByTalk(talkId);

	        if (thisComment.size() == 0) {
		    thisComment = null;
	        }
	        else {
                int listSize = thisComment.size();
	            for (int i =0; i < listSize; i++) {
		            if (thisComment.get(i).getCommentWriter().equals(tokenOwner)) {
			            commentForm.setCommentValid("yes");
		            }
		            else {
			            commentForm.setCommentValid("no");
		            }
		            commentForm.setId(thisComment.get(i).getId());
		            commentForm.setCreated_at(thisComment.get(i).getCreated_at().toString());
		            commentForm.setComment_talk(thisComment.get(i).getCommentTalk());
		            commentForm.setComment_writer(thisComment.get(i).getCommentWriter());
		            commentForm.setContent(thisComment.get(i).getContent());
		            comments.add(commentForm);
		        }
	        }
            talkForm.setMyComments(comments);
            return new ResponseEntity<TalkForm>(talkForm, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    //댓글 작성
    @PostMapping(value="/{roadmap_id}/{talk_id}")
    public ResponseEntity<Comment> writeComment(@PathVariable int roadmap_id, @PathVariable int talk_id,
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
        newComment.setCommentTalk(talk_id);
        newComment.setCommentWriter(tokenOwner);
        newComment.setContent(comment.getContent());
	jpaCommentService.save(newComment);

        return new ResponseEntity<Comment>(newComment, HttpStatus.OK);
    }

    //댓글 수정
    @PutMapping(value="/modify/{roadmap_id}/{talk_id}/{comment_id}")
    public ResponseEntity<Comment> modifyComment(@PathVariable int roadmap_id, @PathVariable int talk_id,
                                                 @PathVariable int comment_id, CommentForm comment,
                                                 @RequestHeader ("Authorization") String token) {

        token = token.substring(7);
        String tokenOwner = jwtTokenUtil.getUsernameFromToken(token);
        Long commentId = (long) comment_id;

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

        newComment.setId(commentId);
        newComment.setCreated_at(time);
        newComment.setCommentTalk(comment.getComment_talk());
        newComment.setCommentWriter(comment.getComment_writer());
        newComment.setContent(comment.getContent());

        Comment cmt = jpaCommentService.update(commentId, newComment);

        if (cmt != null) {
            return new ResponseEntity<Comment>(cmt, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
    //댓글 삭제
}
