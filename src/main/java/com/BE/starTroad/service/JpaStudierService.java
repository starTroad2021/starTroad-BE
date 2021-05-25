package com.BE.starTroad.service;

import com.BE.starTroad.domain.Studier;
import com.BE.starTroad.domain.Study;
import com.BE.starTroad.repository.SpringDataJpaStudierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class JpaStudierService {

    @Autowired
    private SpringDataJpaStudierRepository springDataJpaStudierRepository;


    //스터디 아이디로 몇 명이 스터디에 참여하는지 조회
    public int studyNum(Long studyId) {
        List<Studier> studiers = new ArrayList<>();
        springDataJpaStudierRepository.findByStudyId(studyId.intValue()).forEach(e -> studiers.add(e));
        int num = studiers.size();
        return num;
    }

    public Optional<Studier> findAmIJoin(Long studyId, String email) {
        Optional<Studier> studier = springDataJpaStudierRepository.findByStudyIdAndEmail(studyId, email);
        return studier;
    }

}
