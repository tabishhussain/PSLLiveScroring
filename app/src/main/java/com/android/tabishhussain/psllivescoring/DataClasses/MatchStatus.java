package com.android.tabishhussain.psllivescoring.DataClasses;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Tabish Hussain on 12/24/2015.
 */
public class MatchStatus {

    public String batsman1;
    public String batsman2;
    public String bowler;
    public int inning = -1;
    public String teamA;
    public String teamB;
    public int target = -1;
    public String overs;
    public int teamAScore[] = {-1, -1};
    public int teamBScore[] = {-1, -1};
    public int teamAWickets[] = {-1, -1};
    public int teamBWickets[] = {-1, -1};
    public String matchOverStatement;
    public String matchTimming;
    public String battingTeam = null;
    public String matchTypes = null;
    Pattern scorePattern = Pattern.compile("(\\d+/\\d+)|(\\d+)");
    Pattern overPattern = Pattern.compile("\\d{2}\\.\\d{1}");


    public void setMatchStatus(String de) {
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
        } else if (inning == -1) {
            matchTimming = de;
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
                        teamAWickets[count] = 0;
                    }
                } else {
                    if (score.group().contains("/")) {
                        teamBScore[count] = Integer.parseInt(score.group().split("/")[0]);
                        teamBWickets[count] = Integer.parseInt(score.group().split("/")[1]);
                    } else {
                        teamBScore[count] = Integer.parseInt(score.group());
                        teamBWickets[count] = 0;
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
