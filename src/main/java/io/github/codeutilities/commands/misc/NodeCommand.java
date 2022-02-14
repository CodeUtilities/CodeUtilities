package io.github.codeutilities.commands.misc;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.commands.Command;
import java.util.HashMap;
import java.util.Map.Entry;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;

public class NodeCommand implements Command {

    @Override
    public void register(CommandDispatcher<FabricClientCommandSource> cd) {
        HashMap<String, String> nodes = new HashMap<>();

        nodes.put("1","node1");
        nodes.put("2","node2");
        nodes.put("3","node3");
        nodes.put("4","node4");
        nodes.put("5","node5");
        nodes.put("beta","beta");

        LiteralArgumentBuilder<FabricClientCommandSource> cmd = literal("node");

        for (Entry<String, String> node : nodes.entrySet()) {
            cmd.then(literal(node.getKey()).executes(ctx -> {
                CodeUtilities.MC.player.chat("/server " + node.getValue());
                return 1;
            }));
        }

        cd.register(cmd);
    }
}
