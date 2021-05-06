package com.BE.starTroad.controller;

import com.BE.starTroad.domain.Roadmap;
import com.BE.starTroad.domain.User;
import com.BE.starTroad.repository.SpringDataJpaRoadmapRepository;
import com.BE.starTroad.repository.SpringDataJpaUserRepository;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/home")
public class HomeController {

    @Autowired
    private SpringDataJpaUserRepository springDataJpaUserRepository;
    @Autowired
    private SpringDataJpaRoadmapRepository springDataJpaRoadmapRepository;

    @GetMapping("/{user_email}")
    public ResponseEntity<List<Roadmap>> home(@PathVariable("user_email") String email) {

        Optional<User> user = springDataJpaUserRepository.findByEmail(email);
        if (user.isPresent()) {
            String tags = user.get().getInterest();
            if (tags != null) {
                List<Roadmap> roadmaps = springDataJpaRoadmapRepository.findByTagLike(tags);
                if (roadmaps.isEmpty()) { //태그에 해당하는게 없으면
                    roadmaps = springDataJpaRoadmapRepository.findAll();
                    return new ResponseEntity<List<Roadmap>>(roadmaps, HttpStatus.OK);
                }
            }
            else {
                List<Roadmap> roadmaps = springDataJpaRoadmapRepository.findAll();
                return new ResponseEntity<List<Roadmap>>(roadmaps, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

    }

}