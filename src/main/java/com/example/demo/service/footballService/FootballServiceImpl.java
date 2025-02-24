package com.example.demo.service.footballService;

import com.example.demo.domian.League;
import com.example.demo.domian.Standing;
import com.example.demo.domian.Team;
import com.example.demo.domian.TeamStatistics;
import com.example.demo.dto.response.HomeResponseDTO;
import com.example.demo.dto.response.StandingResponseDTO;
import com.example.demo.global.apipayLoad.code.status.ErrorStatus;
import com.example.demo.global.exception.GeneralException;
import com.example.demo.repository.LeagueRepository;
import com.example.demo.repository.StandingRepository;
import com.example.demo.repository.TeamRepository;
import com.example.demo.repository.TeamStatisticsRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class FootballServiceImpl implements FootballService {

    private final LeagueRepository leagueRepository;
    private final TeamRepository teamRepository;
    private final StandingRepository standingRepository;
    private final TeamStatisticsRepository teamStatisticsRepository;

    @Value("${rapid.api-key}")
    private String RAPID_API_KEY;

    @Override
    public void registerLeagueAndTeamStanding() throws JsonProcessingException {
        JsonNode leagueNode = fetchLeagueData();
        League league = createLeague(leagueNode);
        registerTeamsAndStandingsAndStatistics(leagueNode.path("standings"), league);
    }

    // 순위 정보 최신화 및 팀 통계 정보 최신화
    @Override
    public void updateTeamStatisticsAndStanding() throws JsonProcessingException {
        JsonNode leagueNode = fetchLeagueData();
        League league = leagueRepository.findById(leagueNode.path("id").asLong()).orElseThrow(
                () -> new GeneralException(ErrorStatus.LEAGUE_NOT_FOUND)
        );
        boolean needToUpdate = updateTeamStandings(leagueNode.path("standings"), league);
        if(needToUpdate) updateTeamStatistics(league);
    }

    // 팀 상세 정보 업데이트
    @Override
    public void updateTeamInitInfo(){
        List<Team> teamList = teamRepository.findAll();
        if(teamList.isEmpty()){
            throw new GeneralException(ErrorStatus.INIT_INFO_NOT_FOUND);
        }
        teamList.forEach(
                team -> {
                    JsonNode teamNode = null;
                    try {
                        teamNode = fetchTeamData(team.getId());
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                    team.updateTeamInit(
                            teamNode.path("team").path("country").asText(),
                            teamNode.path("venue").path("city").asText(),
                            teamNode.path("venue").path("name").asText(),
                            teamNode.path("venue").path("image").asText()
                    );
                }
        );
    }

    @Override
    public HomeResponseDTO getHomeResponse() {
        League league = leagueRepository.findById(39L).orElseThrow(
                () -> new GeneralException(ErrorStatus.INIT_INFO_NOT_FOUND)
        );

        List<StandingResponseDTO> standingResponseDTOList = league.getStandings().stream()
                .map(StandingResponseDTO::of)
                .toList();

        return HomeResponseDTO.builder()
                .leagueName(league.getName())
                .leagueLogo(league.getLogo())
                .season(league.getSeason())
                .standingResponseDTOList(standingResponseDTOList)
                .build();
    }

    // RapidAPI를 호출하여 데이터를 가져오는 메소드
    private String requestRapidAPI(String url){
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-rapidapi-key", RAPID_API_KEY);
        headers.set("x-rapidapi-host", "api-football-v1.p.rapidapi.com");

        // HttpEntity를 생성하여 headers 포함
        HttpEntity<String> entity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response;

        try{
            response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        } catch (Exception e) {
            log.error("Failed to call RapidAPI: {}", e.getMessage());
            throw new GeneralException(ErrorStatus.BAD_RAPID_API_REQUEST);
        }

        if(!response.getStatusCode().is2xxSuccessful()){
            log.error("Failed to call RapidAPI: {}", response.getStatusCode());
            throw new GeneralException(ErrorStatus.BAD_RAPID_API_REQUEST);
        }
        else return response.getBody();
    }

    public JsonNode fetchLeagueData() throws JsonProcessingException {
        String responseBody = requestRapidAPI("https://api-football-v1.p.rapidapi.com/v3/standings?league=39&season=2024");
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(responseBody).path("response").get(0).path("league");
    }

    public JsonNode fetchTeamData(Long teamId) throws JsonProcessingException {
        String responseBody = requestRapidAPI("https://api-football-v1.p.rapidapi.com/v3/teams?id=" + teamId);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(responseBody).path("response").get(0);
    }

    public JsonNode fetchTeamStatisticsData(Long teamId) throws JsonProcessingException {
        String responseBody = requestRapidAPI("https://api-football-v1.p.rapidapi.com/v3/teams/statistics?league=39&season=2024&team=" + teamId);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(responseBody).path("response");
    }

    private League createLeague(JsonNode leagueNode) {
        if(leagueRepository.findById(leagueNode.path("id").asLong()).isPresent()){
            log.info("리그 정보가 이미 존재합니다.");
            throw new GeneralException(ErrorStatus.LEAGUE_ALREADY_EXIST);
        }
        return leagueRepository.save(League.builder()
                .id(leagueNode.path("id").asLong())
                .name(leagueNode.path("name").asText())
                .country(leagueNode.path("country").asText())
                .logo(leagueNode.path("logo").asText())
                .season(leagueNode.path("season").asInt())
                .build()
        );
    }

    // 초기 팀, 순위, 통계 정보를 저장한다.
    private void registerTeamsAndStandingsAndStatistics(JsonNode standingsArray, League league) throws JsonProcessingException {
        if(!league.getStandings().isEmpty()){
            throw new GeneralException(ErrorStatus.INIT_INFO_ALREADY_EXIST);
        }
        for (JsonNode standingNode : standingsArray.get(0)) {
            Team team = saveTeam(standingNode.path("team"));
            saveStanding(standingNode, league, team);
            registerTeamStatistics(team, league);
        }
    }

    private void registerTeamStatistics(Team team, League league) throws JsonProcessingException {

        JsonNode statisticsNode = fetchTeamStatisticsData(team.getId());
        teamStatisticsRepository.save(TeamStatistics.builder()
                .team(team)
                .league(league)
                .form(statisticsNode.path("form").asText())
                .total_played(statisticsNode.path("fixtures").path("played").path("total").asInt())
                .home_played(statisticsNode.path("fixtures").path("played").path("home").asInt())
                .away_played(statisticsNode.path("fixtures").path("played").path("away").asInt())
                .total_win(statisticsNode.path("fixtures").path("wins").path("total").asInt())
                .home_win(statisticsNode.path("fixtures").path("wins").path("home").asInt())
                .away_win(statisticsNode.path("fixtures").path("wins").path("away").asInt())
                .total_draw(statisticsNode.path("fixtures").path("draws").path("total").asInt())
                .home_draw(statisticsNode.path("fixtures").path("draws").path("home").asInt())
                .away_draw(statisticsNode.path("fixtures").path("draws").path("away").asInt())
                .total_lose(statisticsNode.path("fixtures").path("loses").path("total").asInt())
                .home_lose(statisticsNode.path("fixtures").path("loses").path("home").asInt())
                .away_lose(statisticsNode.path("fixtures").path("loses").path("away").asInt())
                .total_goal_for(statisticsNode.path("goals").path("for").path("total").path("total").asInt())
                .home_goal_for(statisticsNode.path("goals").path("for").path("total").path("home").asInt())
                .away_goal_for(statisticsNode.path("goals").path("for").path("total").path("away").asInt())
                .total_goal_against(statisticsNode.path("goals").path("against").path("total").path("total").asInt())
                .home_goal_against(statisticsNode.path("goals").path("against").path("total").path("home").asInt())
                .away_goal_against(statisticsNode.path("goals").path("against").path("total").path("away").asInt())
                .build()
        );

    }

    // 순위 정보 최신화
    private boolean updateTeamStandings(JsonNode standingsArray, League league) {
        Map<Long, Standing> standingsMap = league.getStandings().stream()
                .collect(Collectors.toMap(s -> s.getTeam().getId(), s -> s));

        for (JsonNode standingNode : standingsArray.get(0)) {
            Standing standing = standingsMap.get(standingNode.path("team").path("id").asLong());
            if (standing != null) {
                if(standing.getUpdatedAt().equals(standingNode.path("update").asText())){
                    log.info("최신화할 정보가 없습니다.");
                    return false;
                }
                else updateTeamStanding(standing, standingNode);
            }
        }
        return true;
    }

    // 팀 순위 정보를 업데이트하는 메소드
    private void updateTeamStanding(Standing standing, JsonNode standingNode){
        int rank = standingNode.path("rank").asInt();
        int points = standingNode.path("points").asInt();
        int goalsDiff = standingNode.path("goalsDiff").asInt();
        int goalsFor = standingNode.path("goalsFor").asInt();
        int goalsAgainst = standingNode.path("goalsAgainst").asInt();
        int totalPlayed = standingNode.path("all").path("played").asInt();
        int totalWin = standingNode.path("all").path("win").asInt();
        int totalDraw = standingNode.path("all").path("draw").asInt();
        int totalLose = standingNode.path("all").path("lose").asInt();
        String form = standingNode.path("form").asText();
        String description = standingNode.path("description").asText();
        String updatedAt = standingNode.path("update").asText();

        standing.updateStanding(rank, points, goalsDiff, goalsFor, goalsAgainst,
                totalPlayed, totalWin, totalDraw, totalLose, form, description, updatedAt);
    }

    private void updateTeamStatistics(League league){
        teamStatisticsRepository.findAllByLeague(league).forEach(
                teamStatistics -> {
                    JsonNode statisticsNode = null;
                    try {
                        statisticsNode = fetchTeamStatisticsData(teamStatistics.getTeam().getId());
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }

                    teamStatistics.updateTeamStatistics(
                            statisticsNode.path("form").asText(),
                            statisticsNode.path("fixtures").path("played").path("total").asInt(),
                            statisticsNode.path("fixtures").path("played").path("home").asInt(),
                            statisticsNode.path("fixtures").path("played").path("away").asInt(),
                            statisticsNode.path("fixtures").path("wins").path("total").asInt(),
                            statisticsNode.path("fixtures").path("wins").path("home").asInt(),
                            statisticsNode.path("fixtures").path("wins").path("away").asInt(),
                            statisticsNode.path("fixtures").path("draws").path("total").asInt(),
                            statisticsNode.path("fixtures").path("draws").path("home").asInt(),
                            statisticsNode.path("fixtures").path("draws").path("away").asInt(),
                            statisticsNode.path("fixtures").path("loses").path("total").asInt(),
                            statisticsNode.path("fixtures").path("loses").path("home").asInt(),
                            statisticsNode.path("fixtures").path("loses").path("away").asInt(),
                            statisticsNode.path("goals").path("for").path("total").path("total").asInt(),
                            statisticsNode.path("goals").path("for").path("total").path("home").asInt(),
                            statisticsNode.path("goals").path("for").path("total").path("away").asInt(),
                            statisticsNode.path("goals").path("against").path("total").path("total").asInt(),
                            statisticsNode.path("goals").path("against").path("total").path("home").asInt(),
                            statisticsNode.path("goals").path("against").path("total").path("away").asInt()
                    );
                }
        );
    }

    private Team saveTeam(JsonNode teamNode){
        return teamRepository.save(Team.builder()
                .id(teamNode.path("id").asLong())
                .name(teamNode.path("name").asText())
                .logo(teamNode.path("logo").asText())
                .build()
        );
    }

    private void saveStanding(JsonNode standingNode, League league, Team team){
        standingRepository.save(Standing.builder()
                .league(league)
                .team(team)
                .ranking(standingNode.path("rank").asInt())
                .points(standingNode.path("points").asInt())
                .goalsDiff(standingNode.path("goalsDiff").asInt())
                .goalsFor(standingNode.path("goalsFor").asInt())
                .goalsAgainst(standingNode.path("goalsAgainst").asInt())
                .totalPlayed(standingNode.path("all").path("played").asInt())
                .totalWin(standingNode.path("all").path("win").asInt())
                .totalDraw(standingNode.path("all").path("draw").asInt())
                .totalLose(standingNode.path("all").path("lose").asInt())
                .form(standingNode.path("form").asText())
                .description(standingNode.path("description").asText())
                .updatedAt(standingNode.path("update").asText())
                .build()
        );
    }
}