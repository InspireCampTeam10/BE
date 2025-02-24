package com.example.demo.domian;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamStatistics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "league_id")
    private League league;

    private String form;
	private int totalPlayed;
	private int homePlayed;
	private int awayPlayed;
	private int totalWin;
	private int homeWin;
	private int awayWin;
	private int totalDraw;
	private int homeDraw;
	private int awayDraw;
	private int totalLose;
	private int homeLose;
	private int awayLose;
	private int totalGoalFor;
	private int homeGoalFor;
	private int awayGoalFor;
	private int totalGoalAgainst;
	private int homeGoalAgainst;
	private int awayGoalAgainst;

    public void updateTeamStatistics(String form, int totalPlayed, int homePlayed, int awayPlayed, int totalWin, int homeWin, int awayWin, int totalDraw, int homeDraw, int awayDraw, int totalLose, int homeLose, int awayLose, int totalGoalFor, int homeGoalFor, int awayGoalFor, int totalGoalAgainst, int homeGoalAgainst, int awayGoalAgainst) {
        this.form = form;
        this.totalPlayed = totalPlayed;
        this.homePlayed = homePlayed;
        this.awayPlayed = awayPlayed;
        this.totalWin = totalWin;
        this.homeWin = homeWin;
        this.awayWin = awayWin;
        this.totalDraw = totalDraw;
        this.homeDraw = homeDraw;
        this.awayDraw = awayDraw;
        this.totalLose = totalLose;
        this.homeLose = homeLose;
        this.awayLose = awayLose;
        this.totalGoalFor = totalGoalFor;
        this.homeGoalFor = homeGoalFor;
        this.awayGoalFor = awayGoalFor;
        this.totalGoalAgainst = totalGoalAgainst;
        this.homeGoalAgainst = homeGoalAgainst;
        this.awayGoalAgainst = awayGoalAgainst;
    }
}
