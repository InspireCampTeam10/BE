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
	private int total_played;
	private int home_played;
	private int away_played;
	private int total_win;
	private int home_win;
	private int away_win;
	private int total_draw;
	private int home_draw;
	private int away_draw;
	private int total_lose;
	private int home_lose;
	private int away_lose;
	private int total_goal_for;
	private int home_goal_for;
	private int away_goal_for;
	private int total_goal_against;
	private int home_goal_against;
	private int away_goal_against;

    public void updateTeamStatistics(String form, int total_played, int home_played, int away_played, int total_win, int home_win, int away_win, int total_draw, int home_draw, int away_draw, int total_lose, int home_lose, int away_lose, int total_goal_for, int home_goal_for, int away_goal_for, int total_goal_against, int home_goal_against, int away_goal_against) {
        this.form = form;
        this.total_played = total_played;
        this.home_played = home_played;
        this.away_played = away_played;
        this.total_win = total_win;
        this.home_win = home_win;
        this.away_win = away_win;
        this.total_draw = total_draw;
        this.home_draw = home_draw;
        this.away_draw = away_draw;
        this.total_lose = total_lose;
        this.home_lose = home_lose;
        this.away_lose = away_lose;
        this.total_goal_for = total_goal_for;
        this.home_goal_for = home_goal_for;
        this.away_goal_for = away_goal_for;
        this.total_goal_against = total_goal_against;
        this.home_goal_against = home_goal_against;
        this.away_goal_against = away_goal_against;
    }
}
