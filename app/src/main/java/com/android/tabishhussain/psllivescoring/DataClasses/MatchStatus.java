package com.android.tabishhussain.psllivescoring.DataClasses;

import android.text.TextUtils;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Tabish Hussain on 12/24/2015.
 */
public class MatchStatus {

    public String de;
    public String si;
    public String id;
    public String batsman1;
    public String batsman2;
    public String bowler;
    public int inning = 0;
    public String teamA;
    public String teamB;
    public int target = -1;
    public String overs;
    public int teamAScore[] = {-1, -1};
    public int teamBScore[] = {-1, -1};
    public int teamAWickets[] = {-1, -1};
    public int teamBWickets[] = {-1, -1};
    public String matchOverStatement = null;
    public String matchTimming;
    public String battingTeam = null;
    public String matchTypes = null;
    Pattern scorePattern = Pattern.compile("(\\d+/\\d+)|(\\d+)");
    Pattern overPattern = Pattern.compile("\\d+\\.\\d{1}");


    public void setPlayers(String de) {
        Matcher overMatcher = overPattern.matcher(de);
        if (overMatcher.find()) {
            String deSubString = de.substring(de.indexOf("(") + 1, de.indexOf(")"));
            String BBOInfo[] = deSubString.split(",");
            if (BBOInfo.length == 4) {
                batsman1 = BBOInfo[1];
                batsman2 = BBOInfo[2];
                bowler = BBOInfo[3];
            } else {
                batsman1 = BBOInfo[1];
                bowler = BBOInfo[2];
            }
            if (de.contains("Match over")) {
                matchOverStatement = getMatchOverStatement();
            }
        } else if (inning == 0) {
            matchTimming = de;
        }
    }

    public void setInningAndTarget() {
        for (int i = 0; i < 2; i++) {
            if (teamAScore[i] != -1) {
                inning++;
            }
        }
        for (int i = 0; i < 2; i++) {
            if (teamBScore[i] != -1) {
                inning++;
            }
        }
        switch (inning) {
            case 1:
                target = -1;
                break;
            case 2:
                target = battingTeam.equals(teamA) ? teamBScore[0] + 1 : teamAScore[0] + 1;
                break;
            case 3:
                target = -1;
                break;
            case 4:
                target = battingTeam.equals(teamA) ? teamBScore[1] + 1 : teamAScore[1] + 1;

        }
    }

    public String getUrl(String id) {
        return "http://www.espncricinfo.com/ci/engine/match/" + id + ".html";
    }

    public String getMatchOverStatement() {
        if (target > getBattingTeamScore()) {
            return "Match over : " + getBowlingTeam() + "won the match by " + (target - getBattingTeamScore()) + " runs";
        } else if (target < getBattingTeamScore()) {
            return "Match over : " + battingTeam + "won the match by " + (10 - getBattingTeamWickets()) + " wickets";
        } else {
            return "Match Drawn";
        }
    }

    public String getBowlingTeam() {
        return battingTeam.equalsIgnoreCase(teamA) ? teamB : teamA;
    }

    public int getBowlingTeamScore() {
        if (battingTeam.equalsIgnoreCase(teamA)) {
            return (teamBScore[1] != -1) ? teamBScore[1] : teamBScore[0];
        } else {
            return (teamAScore[1] != -1) ? teamAScore[1] : teamAScore[0];
        }
    }

    public int getBowlingTeamWickets() {
        if (battingTeam.equalsIgnoreCase(teamA)) {
            return (teamBWickets[1] != -1) ? teamBWickets[1] : teamBWickets[0];
        } else {
            return (teamAWickets[1] != -1) ? teamAWickets[1] : teamAWickets[0];
        }
    }

    public void setMatchOvers(String de) {
        Matcher overMatcher = overPattern.matcher(de);
        if (overMatcher.find()) {
            overs = overMatcher.group() + " overs";
            Matcher score = scorePattern.matcher(de);
            int count;
            if (matchTypes.equals("test")) {
                count = 1;
            } else {
                count = 0;
            }
            if (score.find()) {
                if (battingTeam.equals(teamA)) {
                    if (score.group().contains("/")) {
                        teamAScore[count] = Integer.parseInt(score.group().split("/")[0]);
                        teamAWickets[count] = Integer.parseInt(score.group().split("/")[1]);
                    } else {
                        teamAScore[count] = Integer.parseInt(score.group());
                        if (!de.contains("Match over")) teamAWickets[count] = 0;
                        else teamAWickets[count] = 10;
                    }
                } else {
                    if (score.group().contains("/")) {
                        teamBScore[count] = Integer.parseInt(score.group().split("/")[0]);
                        teamBWickets[count] = Integer.parseInt(score.group().split("/")[1]);
                    } else {
                        teamBScore[count] = Integer.parseInt(score.group());
                        if (!de.contains("Match over")) teamBWickets[count] = 0;
                        else teamBWickets[count] = 10;
                    }
                }
            }
        }
    }

    public void setTeams(String si) {
        String teamsInfo[] = si.split("v");
        Matcher teamAMatcher = scorePattern.matcher(teamsInfo[0]);
        Matcher teamBMatcher = scorePattern.matcher(teamsInfo[1]);
        int count = 0;
        while (teamAMatcher.find() || teamBMatcher.find()) {
            count++;
        }
        if (count > 2) {
            matchTypes = "test";
        } else {
            matchTypes = "1day";
        }
        teamAMatcher = scorePattern.matcher(teamsInfo[0]);
        teamBMatcher = scorePattern.matcher(teamsInfo[1]);
        int index = 0;
        while (teamAMatcher.find()) {
            if (teamAMatcher.group().contains("/")) {
                teamAScore[index] = Integer.parseInt(teamAMatcher.group().split("/")[0]);
                teamAWickets[index] = Integer.parseInt(teamAMatcher.group().split("/")[1]);
            } else {
                teamAScore[index] = Integer.parseInt(teamAMatcher.group());
                teamAWickets[index] = 0;
            }
            if (index == 0) teamA = teamsInfo[0].substring(0, teamAMatcher.start());
            index++;
        }
        if (index == 0) {
            teamA = teamsInfo[0];
        }
        index = 0;
        while (teamBMatcher.find()) {
            if (teamBMatcher.group().contains("/")) {
                teamBScore[index] = Integer.parseInt(teamBMatcher.group().split("/")[0]);
                teamBWickets[index] = Integer.parseInt(teamBMatcher.group().split("/")[1]);
            } else {
                teamBScore[index] = Integer.parseInt(teamBMatcher.group());
                teamBWickets[index] = 0;
            }
            if (index == 0) teamB = teamsInfo[1].substring(0, teamBMatcher.start());
            index++;
        }
        if (index == 0) {
            teamB = teamsInfo[1];
        }

        if (teamsInfo[0].contains("*")) {
            battingTeam = teamA;
        } else if (teamsInfo[1].contains("*")) {
            battingTeam = teamB;
        } else battingTeam = null;
        setInningAndTarget();
    }

    public int getBattingTeamScore() {
        if (battingTeam.equalsIgnoreCase(teamA)) {
            return (teamAScore[1] != -1) ? teamAScore[1] : teamAScore[0];
        } else {
            return (teamBScore[1] != -1) ? teamBScore[1] : teamBScore[0];
        }
    }

    public int getBattingTeamWickets() {
        if (battingTeam.equalsIgnoreCase(teamA)) {
            return (teamAWickets[1] != -1) ? teamAWickets[1] : teamAWickets[0];
        } else {
            return (teamBWickets[1] != -1) ? teamBWickets[1] : teamBWickets[0];
        }
    }

    public String getMatchStatus() {
        if (matchTypes.equalsIgnoreCase("test")) {
            return si;
        } else if (!TextUtils.isEmpty(matchOverStatement)) {
            return matchOverStatement;
        } else {
            if (target != -1) {
                if (getBattingTeamScore() - target < 0) {
                    return battingTeam + "required " + (target - getBattingTeamScore()) + " runs";
                } else {
                    return "2nd inning :" + battingTeam + "is batting at " + getBattingTeamScore();
                }
            } else {
                return "1st inning :" + battingTeam + "is batting at " + getBattingTeamScore();
            }
        }
    }

    @Override
    public String toString() {
        return "MatchStatus{" +
                "batsman1='" + batsman1 + '\'' +
                ", batsman2='" + batsman2 + '\'' +
                ", bowler='" + bowler + '\'' +
                ", inning=" + inning +
                ", teamA='" + teamA + '\'' +
                ", teamB='" + teamB + '\'' +
                ", target=" + target +
                ", overs='" + overs + '\'' +
                ", teamAScore=" + Arrays.toString(teamAScore) +
                ", teamBScore=" + Arrays.toString(teamBScore) +
                ", teamAWickets=" + Arrays.toString(teamAWickets) +
                ", teamBWickets=" + Arrays.toString(teamBWickets) +
                ", matchOverStatement='" + matchOverStatement + '\'' +
                ", matchTimming='" + matchTimming + '\'' +
                ", battingTeam='" + battingTeam + '\'' +
                ", matchTypes='" + matchTypes + '\'' +
                '}';
    }
}
