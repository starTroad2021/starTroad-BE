package com.BE.starTroad.repository;

import com.BE.starTroad.domain.Studier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpringDataJpaStudierRepository extends JpaRepository<Studier, Long> {

    public List<Studier> findByStudyId(int study_id);
    public int countByStudyId(Long study_id);
    Optional<Studier> findByStudyIdAndEmail(Long studyId, String email);
}
