package io.github.codeutilities.util.df.rank;

import io.github.codeutilities.commands.Command;
import io.github.codeutilities.commands.CommandManager;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;

import java.util.ArrayList;
import java.util.List;

public class DFUtil {
    private static DFRank rank = DFRank.DEFAULT;
    private static final List<Command> regCommands = new ArrayList<>();

    public static DFRank getRank() {
        return rank;
    }

    public static void setRank(DFRank rank) {
        if (rank.getRankWeight() >= DFUtil.rank.getRankWeight()) {
            DFUtil.rank = rank;

            for (Command command : CommandManager.rankedCommands.keySet()) {
                DFRank r = CommandManager.rankedCommands.get(command);

                if (DFUtil.rank.getRankWeight() >= r.getRankWeight()) {
                    if (!regCommands.contains(command)) {
                        regCommands.add(command);
                        command.register(ClientCommandManager.DISPATCHER);
                    }
                }
            }
        }
    }
}
