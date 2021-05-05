package com.BE.starTroad.controller;

import com.BE.starTroad.domain.Roadmap;
import com.BE.starTroad.domain.User;
import com.BE.starTroad.service.JpaRoadmapService;
import org.hibernate.engine.query.spi.OrdinalParameterDescriptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.Optional;

@RestController
@RequestMapping("/roadmap")
public class RoadmapController {

    @Autowired
    JpaRoadmapService jpaRoadmapService;

    @PostMapping(value="/generator")
    public ResponseEntity<Roadmap> generate(Roadmap roadmap) {
        return new ResponseEntity<Roadmap>( jpaRoadmapService.save(roadmap), HttpStatus.OK);
    }

    @GetMapping(value="/{roadmap_id}")
    public ResponseEntity<Roadmap> getInfo(@PathVariable("roadmap_id") Long mapId, Roadmap roadmap) {
        Optional<Roadmap> rdmap = jpaRoadmapService.findById(mapId);

        if (rdmap.isPresent()) {
            return new ResponseEntity<Roadmap>(rdmap.get(),HttpStatus.OK);
        }
        else {
            return new ResponseEntity<Roadmap>((Roadmap)null, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value="/modify/{roadmap_id}")
    public ResponseEntity<Roadmap> modify(@PathVariable("roadmap_id") Long mapId, Roadmap roadmap) {
        Roadmap rdmap = jpaRoadmapService.update(mapId, roadmap);

        if (rdmap != null) {
            return new ResponseEntity<Roadmap>(roadmap, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<Roadmap>((Roadmap)null, HttpStatus.OK);
        }

    }

    @PostMapping(value="/fork")
    public ResponseEntity<Roadmap> fork(Roadmap roadmap) {

        //유저의 이메일이 필요한데 어캐 가져오지
        //starTroad 토큰 복호해서 이메일 까야하나? ㄹㅇ 모르겠음
        String email = "";
        Roadmap rdmap = new Roadmap();
        rdmap = jpaRoadmapService.forkRoadmap(email,roadmap);

        if (rdmap != null) {
            return new ResponseEntity<Roadmap>(roadmap, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<Roadmap>((Roadmap) null, HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping(value="/like")
    public ResponseEntity<Roadmap> like(Roadmap roadmap) {

        jpaRoadmapService.upCount(roadmap);

        return new ResponseEntity<Roadmap>(roadmap, HttpStatus.OK);
    }

    @PostMapping(value="/unlike")
    public ResponseEntity<Roadmap> unlike(Roadmap roadmap) {
        jpaRoadmapService.downCount(roadmap);

        return new ResponseEntity<Roadmap>(roadmap, HttpStatus.OK);
    }


}
