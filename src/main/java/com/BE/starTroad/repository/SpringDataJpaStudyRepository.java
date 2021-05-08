package com.BE.starTroad.repository;

import com.BE.starTroad.domain.Study;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpringDataJpaStudyRepository {

    public List<Study> findByName(String name);
    public List<Study> findByFollow_map(int mapId);
}
