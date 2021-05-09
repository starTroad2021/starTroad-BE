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

@CrossOrigin
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
        newRoadmap.setSummary(roadmap.getSummary());
        newRoadmap.setDescription(roadmap.getDescription());
        newRoadmap.setOwner(tokenOwner);
        newRoadmap.setGenerator(tokenOwner);
        newRoadmap.setInformation(roadmap.getInformation());
        newRoadmap.setLike_count(0);
        newRoadmap.setImage(roadmap.getImage());

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

    @GetMapping(value="/myroadmap")
    public ResponseEntity<List<Roadmap>> myroadmap(@RequestHeader("Authorization") String token) {

        token = token.substring(7);
        String tokenOwner = jwtTokenUtil.getUsernameFromToken(token);

        List<Roadmap> roadmaps = new ArrayList<>();

        roadmaps = jpaRoadmapService.myRoadmaps(tokenOwner);

        return new ResponseEntity<List<Roadmap>>(roadmaps, HttpStatus.OK);
    }

    @GetMapping(value="/{roadmap_id}")
    public ResponseEntity<RoadmapForm> getInfo(@PathVariable("roadmap_id") Long mapId, @RequestHeader("Authorization") String token) {

        token = token.substring(7);
        String tokenOwner = jwtTokenUtil.getUsernameFromToken(token);

        Optional<Roadmap> rdmap = jpaRoadmapService.findById(mapId);

        RoadmapForm rdform = new RoadmapForm();
        if (rdmap.isPresent()) {
            Roadmap roadmap = rdmap.get();
            rdform.setId(mapId.intValue());
            rdform.setName(rdmap.get().getName());
            rdform.setCreated_at(rdmap.get().getCreated_at().toString());
            rdform.setTag(rdmap.get().getTag());
            rdform.setSummary(rdmap.get().getSummary());
            rdform.setDescription(rdmap.get().getDescription());
            rdform.setOwner(rdmap.get().getOwner());
            rdform.setGenerator(rdmap.get().getGenerator());
            rdform.setInformation(rdmap.get().getInformation());
            rdform.setLike_count(rdmap.get().getLike_count());
            rdform.setImage(rdmap.get().getImage());

            if (rdmap.get().getOwner().equals(tokenOwner)) {
                rdform.setValid("yes");
            }
            else {
                rdform.setValid("no");
            }

            return new ResponseEntity<RoadmapForm>(rdform,HttpStatus.OK);
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
        String roadmapOwner = "";

        if (rdMap.isPresent()) {
            roadmapOwner = rdMap.get().getOwner();
        }
        if (!(tokenOwner.equals(roadmapOwner))) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
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
        newRoadmap.setSummary(roadmap.getSummary());
        newRoadmap.setDescription(roadmap.getDescription());
        newRoadmap.setOwner(roadmapOwner);
        newRoadmap.setGenerator(rdMap.get().getGenerator());
        newRoadmap.setInformation(roadmap.getInformation());
        newRoadmap.setLike_count(roadmap.getLike_count());
        newRoadmap.setImage(roadmap.getImage());

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
