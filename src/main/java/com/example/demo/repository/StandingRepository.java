package com.example.demo.repository;

import com.example.demo.domian.Standing;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StandingRepository extends JpaRepository<Standing, Long> {

    List<Standing> findAllByLeagueIdOrderByTeamAsc(Long leagueId);

    Optional<Standing> findByTeamIdAndLeagueId(Long teamId, Long leagueId);
}
