package com.BE.starTroad.service;

import ch.qos.logback.core.pattern.util.RegularEscapeUtil;
import com.BE.starTroad.domain.Request;
import com.BE.starTroad.repository.SpringDataJpaRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class JpaRequestService {

    @Autowired
    private SpringDataJpaRequestRepository springDataJpaRequestRepository;

    public Optional<Request> findByStudyIdAndRequester(Long studyId, String email) {

        Optional<Request> request = springDataJpaRequestRepository.findByStudyIdAndRequester(studyId, email);

        return request;
    }

    public List<Request> findByRequester(String email) {
        List<Request> requests = new ArrayList<>();
        springDataJpaRequestRepository.findByRequester(email).forEach(e -> requests.add(e));

        return requests;
    }

    public Request save(Request request) {
        springDataJpaRequestRepository.save(request);

        return request;
    }

    public List<Request> findByHead(String email) {
        List<Request> requests = new ArrayList<>();
        springDataJpaRequestRepository.findByHead(email).forEach(e -> requests.add(e));

        return requests;
    }

    public Request accept(Long id) {
        Optional<Request> acceptRequest = springDataJpaRequestRepository.findById(id);

        if (acceptRequest.isPresent()) {
            springDataJpaRequestRepository.delete(acceptRequest.get());
        }
        return acceptRequest.get();
    }
}