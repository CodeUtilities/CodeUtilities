package io.github.codeutilities.util.df.rank;

public enum DFRank {
    OWNER("OWNER", 9),
    ADMIN("ADMIN", 8),
    DEV("DEV", 7),
    SRMOD("SR_MOD", 6),
    MOD("MOD", 5),
    JRMOD("JR_MOD", 4),
    SRHELPER("SR_HELPER", 3),
    HELPER("HELPER", 2),
    JRHELPER("JR_HELPER", 1),

    DEFAULT(null, 0);

    private String teamName;
    private int rankWeight;

    DFRank(String teamName, int rankWeight) {
        this.teamName = teamName;
        this.rankWeight = rankWeight;
    }

    public String getTeamName() {
        return teamName;
    }

    public int getRankWeight() {
        return rankWeight;
    }
}
