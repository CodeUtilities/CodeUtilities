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
    private static final List<Command> commands = new ArrayList<>();
    public static final HashMap<Command, HypercubeRank> rankedCommands = new HashMap<>();

    public CommandManager() {
        instance = this;
    }

    public CommandManager getInstance() {
        return instance;
    }

    @Override
    public void load() {
    	
    	// item commands
    	commands.add(new BreakableCommand());
        commands.add(new CodeVaultCommand());
        commands.add(new DfGiveCommand());
        commands.add(new EditNbtCommand());
        //commands.add(new ImportFileCommand());
        commands.add(new RelativeLocCommand());
        commands.add(new TemplatesCommand());
        commands.add(new UnpackCommand());
        commands.add(new WebviewCommand());
        
        // misc commands
        commands.add(new AfkCommand());
        commands.add(new CalcCommand());
        commands.add(new CodeUtilitiesCommand());
        commands.add(new NBSCommand());
        //commands.add(new NBSSearchCommand());
        commands.add(new NodeCommand());
        commands.add(new PartnerBracketCommand());
        commands.add(new PingCommand());
        commands.add(new PJoinCommand());
        commands.add(new SchemCommand());
        commands.add(new ScriptsCommand());
        commands.add(new SearchCommand());
        commands.add(new QueueCommand());
        
        // text commands
        commands.add(new ActionbarCommand());
        commands.add(new ColorCommand());
        commands.add(new CopyTextCommand());
        commands.add(new SubTitleCommand());
        commands.add(new TitleCommand());
        commands.add(new UUIDCommand());
        
        
        
        
        
        
        
        

        // Example of registering commands with a required df rank
        // rankedCommands.put(new TestCommand(), DFRank.JRHELPER);

        attachTo(ClientCommandManager.DISPATCHER);
    }

    private void attachTo(CommandDispatcher<FabricClientCommandSource> cd) {
        for (Command command : commands) {
            command.register(cd);
        }
    }

    public static List<Command> getCommands() { return commands; }
}
