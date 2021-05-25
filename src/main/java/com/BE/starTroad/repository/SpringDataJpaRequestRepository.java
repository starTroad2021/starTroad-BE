package com.BE.starTroad.repository;

import com.BE.starTroad.domain.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpringDataJpaRequestRepository extends JpaRepository<Request, Long> {

    public Optional<Request> findByStudyIdAndRequester(Long studyId, String email);
    public List<Request> findByRequester(String email);
    public List<Request> findByHead(String email);
}
