package io.github.codeutilities.util.hypercube.rank;

import io.github.codeutilities.commands.Command;
import io.github.codeutilities.commands.CommandManager;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class HypercubeUtil {
    private static HypercubeRank rank = HypercubeRank.DEFAULT;
    private static final List<Command> regCommands = new ArrayList<>();

    public static HypercubeRank getRank() {
        return rank;
    }

    public static void setRank(HypercubeRank rank) {
        HypercubeUtil.rank = rank;

        for (Command command : CommandManager.rankedCommands.keySet()) {
            HypercubeRank r = CommandManager.rankedCommands.get(command);

            if (rank.hasPermission(r)) {
                if (!regCommands.contains(command)) {
                    regCommands.add(command);
                    command.register(ClientCommandManager.DISPATCHER);
                }
            }
        }
    }

    public static final Identifier channel = new Identifier("hypercube", "codeutilities");
}
