package com.example.demo.repository;

import com.example.demo.domian.League;
import com.example.demo.domian.TeamStatistics;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamStatisticsRepository extends JpaRepository<TeamStatistics, Long> {

    List<TeamStatistics> findAllByLeague(League league);
}
