package com.example.demo.repository;

import com.example.demo.domian.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {
    // 특정 유저의 FitNews 데이터를 최신순으로 6개 조회
    Optional<List<News>> findTop6ByUserIdOrderByTimestampDesc(Long userId);
}
