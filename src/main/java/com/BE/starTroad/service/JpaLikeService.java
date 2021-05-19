package com.BE.starTroad.service;

import com.BE.starTroad.domain.Like;
import com.BE.starTroad.repository.SpringDataJpaLikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JpaLikeService {

    @Autowired
    private SpringDataJpaLikeRepository springDataJpaLikeRepository;

    public void pushLike(Like like) {
        springDataJpaLikeRepository.save(like);
    }

    public void unpushLike(Long id) {

        springDataJpaLikeRepository.deleteById(id);
    }

    public Optional<Like> findByEmailAndRoadmap_id(String email, int mapId) {
        Optional<Like> like = springDataJpaLikeRepository.findByEmailAndRoadmapId(email, mapId);

        return like;
    }
}
