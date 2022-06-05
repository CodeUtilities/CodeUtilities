package io.github.codeutilities.commands.misc;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.commands.Command;
import io.github.codeutilities.commands.arguments.StringFuncArgumentType;
import io.github.codeutilities.screen.script.ScriptListScreen;
import io.github.codeutilities.script.Script;
import io.github.codeutilities.script.ScriptManager;
import io.github.codeutilities.script.values.ScriptValue;
import io.github.codeutilities.util.chat.ChatUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;

public class ScriptsCommand implements Command {

    @Override
    public void register(CommandDispatcher<FabricClientCommandSource> cd) {
        cd.register(
            literal("scripts")
                .executes(ctx -> {
                    CodeUtilities.MC.send(() -> CodeUtilities.MC.setScreen(new ScriptListScreen()));
                    return 0;
                })
                .then(literal("reload")
                    .executes(ctx -> {
                        ScriptManager.getInstance().reload();
                        ChatUtil.info("Scripts reloaded!");
                        return 0;
                    })
                )
                .then(literal("vars")
                    .then(argument("script", new StringFuncArgumentType((v) -> {
                            List<String> possible = new ArrayList<>();
                            for (Script s : ScriptManager.getInstance().getScripts()) {
                                possible.add(s.getName().replaceAll(" ", "_"));
                            }
                            return possible;
                            }, false)
                        )
                        .executes(ctx -> {
                            listVars(ctx.getArgument("script", String.class), "");
                            return 0;
                        })
                        .then(argument("filter", StringArgumentType.greedyString())
                            .executes(ctx -> {
                                listVars(ctx.getArgument("script", String.class), ctx.getArgument("filter", String.class));
                                return 0;
                            })
                        )
                    )
                )
        );
    }

    private void listVars(String script, String filter) {
        for (Script s : ScriptManager.getInstance().getScripts()) {
            if (s.getName().replaceAll(" ", "_").equals(script)) {
                List<Entry<String, ScriptValue>> vars = s.getContext().listVariables(filter);

                int showing = Math.min(vars.size(), 50);
                int filtered = vars.size();
                int total = s.getContext().getVariableCount();

                ChatUtil.info("Script " + s.getName() + " has a total of " + total + " variables.");

                if (filter.isEmpty()) {
                    ChatUtil.info("Showing " + showing + " variables.");
                } else {
                    ChatUtil.info("Showing " + showing + " of " + filtered + " filtered variables.");
                }

                for (int i = 0; i < showing; i++) {
                    Entry<String, ScriptValue> e = vars.get(i);
                    ChatUtil.info(e.getKey() + ": " + e.getValue().asText());
                }
                return;
            }
        }
        ChatUtil.error("Unknown script!");
    }

    @Override
    public String getDescription() {
        return "[blue]/scripts[reset]\n" +
                "\n" +
                "Opens a GUI to edit custom CodeUtilities scripts.\n";
    }

    @Override
    public String getName() {
        return "/scripts";
    }
}
