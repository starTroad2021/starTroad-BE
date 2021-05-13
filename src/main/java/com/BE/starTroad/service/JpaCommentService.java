package com.BE.starTroad.service;

import com.BE.starTroad.domain.Comment;
import com.BE.starTroad.repository.SpringDataJpaCommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class JpaCommentService {

    @Autowired
    private SpringDataJpaCommentRepository springDataJpaCommentRepository;


    //talk 아이디로 comment 조회
    public List<Comment> findByTalk(int talkId){
        List<Comment> comments = new ArrayList<>();
        springDataJpaCommentRepository.findById(talkId).forEach(e -> comments.add(e));

        return comments;
    }
}
