package com.BE.starTroad.service;

import com.BE.starTroad.domain.Talk;
import com.BE.starTroad.repository.SpringDataJpaTalkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class JpaTalkService {

    @Autowired
    private SpringDataJpaTalkRepository springDataJpaTalkRepository;

    public List<Talk> findAll() {
        List<Talk> talks = new ArrayList<>();
        springDataJpaTalkRepository.findAll().forEach(e -> talks.add(e));
        return talks;
    }

    public List<Talk> findByTalk_Roadmap(int mapId) {
        List<Talk> talks = new ArrayList<>();
        springDataJpaTalkRepository.findByTalk_Roadmap(mapId).forEach(e -> talks.add(e));
        return talks;
    }

    public Talk save(Talk talk) {
        springDataJpaTalkRepository.save(talk);
        return talk;
    }

    public Optional<Talk> findById(int talkId) {
        Long id = (long) talkId;
        Optional<Talk> talk = springDataJpaTalkRepository.findById(id);
        return talk;
    }
}
