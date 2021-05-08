package com.BE.starTroad.controller;

import com.BE.starTroad.config.JwtTokenUtil;
import com.BE.starTroad.domain.Roadmap;
import com.BE.starTroad.domain.User;
import com.BE.starTroad.service.JpaRoadmapService;
import org.hibernate.engine.query.spi.OrdinalParameterDescriptor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.print.attribute.standard.JobSheets;
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
    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @PostMapping(value="/generate")
    public ResponseEntity<Roadmap> generate(RoadmapForm roadmap, @RequestHeader("Authorization") String token) {

        token = token.substring(7);
        String tokenOwner = jwtTokenUtil.getUsernameFromToken(token);

        String roadmapGenerater = roadmap.getGenerator();

        if (!(tokenOwner.equals(roadmapGenerater))) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

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
        newRoadmap.setOwner(tokenOwner);
        newRoadmap.setGenerator(tokenOwner);
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

    @GetMapping(value="/{roadmap_id}")
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
    public ResponseEntity<Roadmap> modify(@PathVariable("roadmap_id") Long mapId, RoadmapForm roadmap,
                                          @RequestHeader("Authorization") String token) {

        token = token.substring(7);
        String tokenOwner = jwtTokenUtil.getUsernameFromToken(token);
        Optional<Roadmap> rdMap = jpaRoadmapService.findById(mapId);
        String roadMapOwner = "";
        if (rdMap.isPresent()) {
            roadMapOwner = rdMap.get().getOwner();
        }
        if (tokenOwner != roadMapOwner) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

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

        Roadmap rdmap = jpaRoadmapService.update(mapId, newRoadmap);

        if (rdmap != null) {
            return new ResponseEntity<Roadmap>(rdmap, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(null, HttpStatus.OK);
        }

    }

    @PostMapping(value="/fork")
    public ResponseEntity<Roadmap> fork(Long id, @RequestHeader("Authorization") String token) {

        token = token.substring(7);
        String tokenOwner = jwtTokenUtil.getUsernameFromToken(token);

        Roadmap rdmap = new Roadmap();
        System.out.println(id);
        Optional<Roadmap> roadmap = jpaRoadmapService.findById(id);

        if (roadmap.isPresent()) {
            rdmap = jpaRoadmapService.forkRoadmap(tokenOwner, roadmap.get());

            if (rdmap != null) {
                return new ResponseEntity<Roadmap>(rdmap, HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
        }
        else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(value="/like")
    public ResponseEntity<Roadmap> like(Long id) {

        Optional<Roadmap> roadmap = jpaRoadmapService.findById(id);

        if (roadmap.isPresent()) {
            jpaRoadmapService.upCount(roadmap.get());
            return new ResponseEntity<Roadmap>(roadmap.get(), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<Roadmap>(roadmap.get(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value="/unlike")
    public ResponseEntity<Roadmap> unlike(Long id) {

        Optional<Roadmap> roadmap = jpaRoadmapService.findById(id);

        if (roadmap.isPresent()) {
            jpaRoadmapService.downCount(roadmap.get());
            return new ResponseEntity<Roadmap>(roadmap.get(), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<Roadmap>(roadmap.get(), HttpStatus.BAD_REQUEST);
        }

    }


}
