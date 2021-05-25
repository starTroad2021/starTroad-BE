package com.BE.starTroad.repository;

import com.BE.starTroad.domain.Study;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpringDataJpaStudyRepository extends JpaRepository<Study, Long> {

    public List<Study> findByName(String name);
    public List<Study> findByFollowMap(int mapId);
    public List<Study> findByLeader(String email);

}
