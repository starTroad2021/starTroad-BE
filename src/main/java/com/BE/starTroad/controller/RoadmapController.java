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
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/roadmap")
public class RoadmapController {

    @Autowired
    JpaRoadmapService jpaRoadmapService;

    @PostMapping(value="/generator")
    public ResponseEntity<Roadmap> generate(RoadmapForm roadmap) {

        String timestamp = roadmap.getCreated_at();
        Timestamp time;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
            Date parsedDate = dateFormat.parse(timestamp);
            time = new Timestamp(parsedDate.getTime());
        } catch(Exception e) {
            time = new Timestamp(System.currentTimeMillis());
        }
        Roadmap newRoadmap = new Roadmap();

        newRoadmap.setName(roadmap.getName());
        newRoadmap.setCreated_at(time);
        newRoadmap.setTag(roadmap.getTag());
        newRoadmap.setDescription(roadmap.getDescription());
        newRoadmap.setOwner(roadmap.getOwner());
        newRoadmap.setGenerator(roadmap.getGenerator());
        newRoadmap.setInformation(roadmap.getInformation());
        newRoadmap.setLike_count(roadmap.getLike_count());

        return new ResponseEntity<Roadmap>(jpaRoadmapService.save(newRoadmap), HttpStatus.OK);
    }

    @GetMapping(value="/search")
    public ResponseEntity<List<Roadmap>> search(String name, String tag) {

        List<Roadmap> roadmapsName = new ArrayList<>();
        List<Roadmap> roadmapsTag = new ArrayList<>();
        List<Roadmap> roadmaps = new ArrayList<>();

        roadmapsName = jpaRoadmapService.findByName(name);
        roadmapsTag = jpaRoadmapService.findByTag(tag);

        roadmaps.addAll(roadmapsName);
        roadmaps.addAll(roadmapsTag);

        return new ResponseEntity<List<Roadmap>> (roadmaps, HttpStatus.OK);
    }

    @GetMapping(value="/detail/{roadmap_id}")
    public ResponseEntity<Roadmap> getInfo(@PathVariable("roadmap_id") Long mapId) {
        Optional<Roadmap> rdmap = jpaRoadmapService.findById(mapId);
        if (rdmap.isPresent()) {
            Roadmap roadmap = rdmap.get();
            return new ResponseEntity<Roadmap>(roadmap,HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value="/modify/{roadmap_id}")
    public ResponseEntity<Roadmap> modify(@PathVariable("roadmap_id") Long mapId, Roadmap roadmap) {
        Roadmap rdmap = jpaRoadmapService.update(mapId, roadmap);

        if (rdmap != null) {
            return new ResponseEntity<Roadmap>(roadmap, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(null, HttpStatus.OK);
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
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
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
