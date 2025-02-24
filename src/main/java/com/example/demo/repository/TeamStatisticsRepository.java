package com.example.demo.repository;

import com.example.demo.domian.League;
import com.example.demo.domian.Team;
import com.example.demo.domian.TeamStatistics;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TeamStatisticsRepository extends JpaRepository<TeamStatistics, Long> {

    List<TeamStatistics> findAllByLeague(League league);

    Optional<TeamStatistics> findByTeamIdAndLeagueId(Long teamId, Long leagueId);
}
