package io.github.codeutilities.commands.item;

import java.util.Arrays;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;

import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.commands.Command;
import io.github.codeutilities.commands.arguments.StringListArgumentType;
import io.github.codeutilities.util.ItemUtil;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class RelativeLocCommand implements Command {

    private static final String[] TARGET_TYPES = {"selection", "default", "damager", "killer", "victim", "shooter", "projectile"};

    public void register(CommandDispatcher<FabricClientCommandSource> cd) {
    	MinecraftClient mc = CodeUtilities.MC;
        cd.register(literal("relativeloc")
                .then(argument("target", StringListArgumentType.string(TARGET_TYPES))
                        .then(argument("forwards", FloatArgumentType.floatArg())
                                .then(argument("upwards", FloatArgumentType.floatArg())
                                        .then(argument("right", FloatArgumentType.floatArg())
                                                .then(argument("rot_down", FloatArgumentType.floatArg())
                                                        .then(argument("rot_right", FloatArgumentType.floatArg())
                                                                .executes(ctx -> {
                                                                    if (mc.player.isCreative()) {
                                                                        String target = ctx.getArgument("target", String.class);
                                                                        Float forwards = ctx.getArgument("forwards", float.class);
                                                                        Float upwards = ctx.getArgument("upwards", float.class);
                                                                        Float right = ctx.getArgument("right", float.class);
                                                                        Float rot_down = ctx.getArgument("rot_down", float.class);
                                                                        Float rot_right = ctx.getArgument("rot_right", float.class);
                                                                        return this.run(target, forwards, upwards, right, rot_down, rot_right);
                                                                    } else {
                                                                        return -1;
                                                                    }
                                                                })
                                                        )
                                                )
                                        )
                                )
                        )
                )
        );
    }

    /*
    @Override
    public String getDescription() {
        return "[blue]/relativeloc <target> <forwards> <upwards> <right> <rot_down> <rot_right>[reset]\n"
                + "\n"
                + "Gives you the Relative Location item that you can use in your code."
                + "[red]Disclaimer[reset]: Relative Locations are deprecated code item that is not recommended to use. Use them on your own risk.";
    }

    @Override
    public String getName() {
        return "/relativeloc";
    }
    
    */

    private int run(String target, float forwards, float upwards, float right, float rot_down, float rot_right) {
        String[] targetNames = {"Selected Object", "Default", "Damager", "Killer", "Victim", "Shooter", "Projectile"};
        String finalTarget = targetNames[Arrays.asList(TARGET_TYPES).indexOf(target)];

        NbtCompound publicBukkitNBT = new NbtCompound();
        NbtCompound itemNBT = new NbtCompound();
        NbtCompound codeNBT = new NbtCompound();
        NbtCompound dataNBT = new NbtCompound();

        dataNBT.putString("target", finalTarget);
        dataNBT.putFloat("forward", forwards);
        dataNBT.putFloat("up", upwards);
        dataNBT.putFloat("right", right);
        dataNBT.putFloat("rot_down", rot_down);
        dataNBT.putFloat("rot_right", rot_right);
        codeNBT.put("data", dataNBT);

        codeNBT.putString("id", "r_loc");
        publicBukkitNBT.putString("hypercube:varitem", codeNBT.toString());

        ItemStack item = new ItemStack(Items.PAPER);
        itemNBT.put("PublicBukkitValues", publicBukkitNBT);

        itemNBT.putInt("CustomModelData", 500);

        item.setNbt(itemNBT);

        NbtCompound display = new NbtCompound();
        NbtList lore = new NbtList();

        LiteralText itemName = new LiteralText("Relative Location");
        itemName.setStyle(Style.EMPTY.withColor(Formatting.GREEN).withItalic(false));
        display.put("Name", NbtString.of(Text.Serializer.toJson(itemName)));

        LiteralText lore1 = new LiteralText("Target: ");
        LiteralText lore2 = new LiteralText(finalTarget);
        lore1.setStyle(Style.EMPTY.withColor(Formatting.GRAY).withItalic(false));
        lore2.setStyle(Style.EMPTY.withColor(Formatting.WHITE).withItalic(false));
        lore.add(0, NbtString.of(Text.Serializer.toJson(lore1.append(lore2))));

        lore1 = new LiteralText("Forwards: ");
        craftLore(forwards, lore, lore1);

        lore1 = new LiteralText("Upwards: ");
        craftLore(upwards, lore, lore1);

        lore1 = new LiteralText("Right: ");
        craftLore(right, lore, lore1);

        lore1 = new LiteralText("Rot Down: ");
        craftLore(rot_down, lore, lore1);

        lore1 = new LiteralText("Rot Right: ");
        craftLore(rot_right, lore, lore1);

        display.put("Lore", lore);
        item.getNbt().put("display", display);

        ItemUtil.giveCreativeItem(item, true);
        return 1;
    }

    private void craftLore(float upwards, NbtList lore, LiteralText lore1) {
        LiteralText lore2;
        lore2 = new LiteralText("" + upwards);
        lore1.setStyle(Style.EMPTY.withColor(Formatting.GRAY).withItalic(false));
        lore2.setStyle(Style.EMPTY.withColor(Formatting.WHITE).withItalic(false));
        lore.add(lore.size(), NbtString.of(Text.Serializer.toJson(lore1.append(lore2))));
    }
}
