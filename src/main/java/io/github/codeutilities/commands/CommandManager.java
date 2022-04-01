package io.github.codeutilities.commands;

import com.mojang.brigadier.CommandDispatcher;
import io.github.codeutilities.commands.item.*;
import io.github.codeutilities.commands.misc.*;
import io.github.codeutilities.commands.text.*;
import io.github.codeutilities.loader.Loadable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.github.codeutilities.util.hypercube.rank.HypercubeRank;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;

public class CommandManager implements Loadable {

    private final CommandManager instance;
    private final List<Command> commands = new ArrayList<>();
    public static final HashMap<Command, HypercubeRank> rankedCommands = new HashMap<>();

    public CommandManager() {
        instance = this;
    }

    public CommandManager getInstance() {
        return instance;
    }

    @Override
    public void load() {
        commands.add(new DfGiveCommand());
        commands.add(new UUIDCommand());
        commands.add(new NodeCommand());
        commands.add(new EditNbtCommand());
        commands.add(new ScriptsCommand());
        commands.add(new WebviewCommand());
        commands.add(new NBSCommand());
        commands.add(new AfkCommnd());

        // Example of registering commands with a required df rank
        // rankedCommands.put(new TestCommand(), DFRank.JRHELPER);

        attachTo(ClientCommandManager.DISPATCHER);
    }

    private void attachTo(CommandDispatcher<FabricClientCommandSource> cd) {
        for (Command command : commands) {
            command.register(cd);
        }
    }
}
