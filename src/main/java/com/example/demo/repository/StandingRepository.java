package com.example.demo.repository;

import com.example.demo.domian.Standing;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StandingRepository extends JpaRepository<Standing, Long> {

    public List<Standing> findAllByLeagueIdOrderByTeamAsc(Long leagueId);
}
