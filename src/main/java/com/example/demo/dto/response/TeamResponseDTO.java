package com.example.demo.dto.response;

import com.example.demo.domian.Standing;
import com.example.demo.domian.Team;
import com.example.demo.domian.TeamStatistics;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class TeamResponseDTO {

    private Long id;
    private String name;
    private String country;
    private String logo;
    private String city;
    private String venueName;
    private String venueImage;

    private int ranking;
    private int goalFor;
    private int goalAgainst;
    private int goalDifference;
    private int points;
    private String description;

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
    private int homeGoalFor;
    private int awayGoalFor;
    private int homeGoalAgainst;
    private int awayGoalAgainst;

    public static TeamResponseDTO of(Team team, Standing standing, TeamStatistics teamStatistics){
        return TeamResponseDTO.builder()
                .id(team.getId())
                .name(team.getName())
                .country(team.getCountry())
                .logo(team.getLogo())
                .city(team.getCity())
                .venueName(team.getVenueName())
                .venueImage(team.getVenueImage())

                .ranking(standing.getRanking())
                .goalFor(standing.getGoalsFor())
                .goalAgainst(standing.getGoalsAgainst())
                .goalDifference(standing.getGoalsDiff())
                .points(standing.getPoints())
                .description(standing.getDescription())

                .form(teamStatistics.getForm())
                .totalPlayed(teamStatistics.getTotalPlayed())
                .homePlayed(teamStatistics.getHomePlayed())
                .awayPlayed(teamStatistics.getAwayPlayed())
                .totalWin(teamStatistics.getTotalWin())
                .homeWin(teamStatistics.getHomeWin())
                .awayWin(teamStatistics.getAwayWin())
                .totalDraw(teamStatistics.getTotalDraw())
                .homeDraw(teamStatistics.getHomeDraw())
                .awayDraw(teamStatistics.getAwayDraw())
                .totalLose(teamStatistics.getTotalLose())
                .homeLose(teamStatistics.getHomeLose())
                .awayLose(teamStatistics.getAwayLose())
                .homeGoalFor(teamStatistics.getHomeGoalFor())
                .awayGoalFor(teamStatistics.getAwayGoalFor())
                .homeGoalAgainst(teamStatistics.getHomeGoalAgainst())
                .awayGoalAgainst(teamStatistics.getAwayGoalAgainst())
                .build();
    }
}
