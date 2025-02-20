package com.example.demo.domian;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Standing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "league_id")
    private League league;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    private int ranking;

    private int points;

    private int goalsDiff;
    private int goalsFor;
    private int goalsAgainst;

    private int totalPlayed;
    private int totalWin;
    private int totalDraw;
    private int totalLose;

    // 최근 5경기 성적
    private String form;

    // 유럽대항전, 강등권 등 표시
    private String description;
}
