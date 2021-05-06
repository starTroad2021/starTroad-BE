package com.BE.starTroad.service;

import com.BE.starTroad.domain.Roadmap;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import java.awt.*;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class JpaRoadmapServiceTest {

    @Autowired
    JpaRoadmapService jpaRoadmapService;

    @Test
    void 로드맵생성() {
        Roadmap roadmap = new Roadmap();


    }

    @Test
    void 로드맵업데이트() {
    }

    @Test
    void 로드맵_아이디로_찾기() {

    }

    @Test
    void 로드맵_전체_조회() {
    }

    @Test
    void 로드맵_이름으로_찾기() {
    }

    @Test
    void 로드맵_태그로_찾기() {
    }

    @Test
    void 로드맵포크() {
    }

    @Test
    void 로드맵_좋아요() {
    }

    @Test
    void 로드맵_좋아요_취소() {
    }

    @Test
    void 내로드맵조회() {
    }
}