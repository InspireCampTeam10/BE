package com.example.demo.domian;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
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

    private String updatedAt;

    public void updateStanding(int ranking, int points, int goalsDiff, int goalsFor, int goalsAgainst, int totalPlayed, int totalWin, int totalDraw, int totalLose, String form, String description, String updatedAt) {
        this.ranking = ranking;
        this.points = points;
        this.goalsDiff = goalsDiff;
        this.goalsFor = goalsFor;
        this.goalsAgainst = goalsAgainst;
        this.totalPlayed = totalPlayed;
        this.totalWin = totalWin;
        this.totalDraw = totalDraw;
        this.totalLose = totalLose;
        this.form = form;
        this.description = description;
        this.updatedAt = updatedAt;
    }
}
