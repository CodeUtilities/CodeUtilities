package io.github.codeutilities.commands;

import com.mojang.brigadier.CommandDispatcher;
import io.github.codeutilities.commands.item.DfGiveCommand;
import io.github.codeutilities.commands.item.EditNbtCommand;
import io.github.codeutilities.commands.misc.NodeCommand;
import io.github.codeutilities.commands.text.UUIDCommand;
import io.github.codeutilities.loader.Loadable;
import java.util.ArrayList;
import java.util.List;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;

public class CommandManager implements Loadable {

    private CommandManager instance;
    private final List<Command> commands = new ArrayList<>();

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

        attachTo(ClientCommandManager.DISPATCHER);
    }

    private void attachTo(CommandDispatcher<FabricClientCommandSource> cd) {
        for (Command command : commands) {
            command.register(cd);
        }
    }
}
