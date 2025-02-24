package com.example.demo.dto.response;

import com.example.demo.domian.Standing;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StandingResponseDTO {

    private String teamName;
    private String logo;
    private String form;
    private int ranking;
    private int totalPlayed;
    private int totalWin;
    private int totalDraw;
    private int totalLose;
    private int goalFor;
    private int goalAgainst;
    private int goalDifference;
    private int points;
    private String description;
    private String updatedAt;

    public static StandingResponseDTO of(Standing standing) {
        return StandingResponseDTO.builder()
                .teamName(standing.getTeam().getName())
                .logo(standing.getTeam().getLogo())
                .form(standing.getForm())
                .ranking(standing.getRanking())

                .totalPlayed(standing.getTotalPlayed())
                .totalWin(standing.getTotalWin())
                .totalDraw(standing.getTotalDraw())
                .totalLose(standing.getTotalLose())

                .goalFor(standing.getGoalsFor())
                .goalAgainst(standing.getGoalsAgainst())
                .goalDifference(standing.getGoalsDiff())
                .points(standing.getPoints())
                .description(standing.getDescription())
                .updatedAt(standing.getUpdatedAt())
                .build();
    }
}
