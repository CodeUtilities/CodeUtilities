package io.github.codeutilities.screen.commands.codevault;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.features.tab.CodeUtilitiesServer;
import io.github.codeutilities.features.tab.WebMessage;
import io.github.codeutilities.screen.CScreen;
import io.github.codeutilities.screen.widget.*;
import io.github.codeutilities.util.ItemUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtInt;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;

public class CodeVaultScreen extends CScreen {

    String[] categories = {
        "All",
        "Soft-Coding",
        "Concepts",
        "Commands",
        "Tools",
        "Game Mechanics",
        "Misc",
        "Var Manipulation",
        "NBS Songs"
    };
    String category = "all";

    List<ItemStack> items = new ArrayList<>();
    CScrollPanel panel;
    CTextField searchBox;
    List<CButton> categoryBtns = new ArrayList<>();

    public CodeVaultScreen() {
        super(100, 100);
        loadScreen();
        open();
    }

    private void open() {
        CPlainPanel root = new CPlainPanel(0, 0, 100, 90);



        panel = new CScrollPanel(0, 0, 100, 90);

        fillPanel(items);
        root.add(panel);

        int y = 10;
        for (String category : categories) {
            CButton btn = new CButton(0, y, 44, 10, category, () -> {});
            widgets.add(btn);
            categoryBtns.add(btn);

            btn.setOnClick(() -> {
                this.category = category.toLowerCase();
                update();
            });


            y += 10;
        }

        searchBox = new CTextField("Search...", 0, 0, 100, 10, true);
        searchBox.setChangedListener(this::update);
        widgets.add(searchBox);

        widgets.add(root);
    }

    private void update() {
        List<ItemStack> filtered = new ArrayList<>();

        String query = searchBox.getText().toLowerCase();



        for (ItemStack item : items) {
            if (item.getName().getString().toLowerCase().contains(query)) {
                if (Objects.equals(category, "all")) {
                    filtered.add(item);
                } else {
                    String category = item.getNbt().getString("category");

                    if (Objects.equals(category, this.category)) {
                        filtered.add(item);
                    }
                }
            }
        }

        panel.clear();
        fillPanel(filtered);
    }

    private void fillPanel(List<ItemStack> itemList) {
        int x_offset = 47;
        int y_offset = 16;
        for (ItemStack stack : itemList) {
            CItem item = new CItem(x_offset, y_offset, stack);

            item.setClickListener((i) -> {
                ItemUtil.giveCreativeItem(stack, true);
                CodeUtilities.MC.player.playSound(SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 2, 1);
            });

            panel.add(item);

            if (x_offset == 87) {
                x_offset = 47;
                y_offset += 8;
            } else {
                x_offset += 10;
            }
        }
    }

    private void loadScreen() {
        CodeUtilitiesServer.requestMessage(new WebMessage("code-vault"), message -> {
            String[] ranks = {"None", "Nobel", "Emperor", "Mythic", "Overlord"};
            String[] plots = {"Basic", "Large", "Massive"};

            HashMap<String, String> categoryConv = new HashMap<>();
            categoryConv.put("&eCommands", "Commands");
            categoryConv.put("&2Note Block Songs","NBS Songs");
            categoryConv.put("&dSoft-Coding","Soft-Coding");
            categoryConv.put("&aTools","Tools");
            categoryConv.put("&eVariable Manipulation","Var Manipulation");
            categoryConv.put("&7Gameplay Mechanics","Game Mechanics");
            categoryConv.put("&3Concepts","Concepts");
            categoryConv.put("&cMisc.","Misc");

            JsonObject content = message.getContent().getAsJsonObject();
            for (Entry<String, JsonElement> k : content.entrySet()) {
                try {
                    JsonArray arr = k.getValue().getAsJsonArray();
                    int plotsize = Integer.parseInt(arr.get(0).getAsString());
                    String category = arr.get(1).getAsString();
                    String templatedata = arr.get(2).getAsString();//todo: add to item nbt
                    String lore = arr.get(3).getAsString();
                    String author = arr.get(4).getAsString();
                    int rank = Integer.parseInt(arr.get(5).getAsString());
                    String name = arr.get(6).getAsString();
                    String material = arr.get(7).getAsString();

                    ItemStack item = new ItemStack(Registry.ITEM.get(new Identifier(material)));
                    item.setCustomName(new LiteralText(name));

                    NbtList loreTag = new NbtList();

                    loreTag.add(NbtString.of(Text.Serializer.toJson(new LiteralText(
                        "§7Created by §a" + author
                    ))));
                    loreTag.add(NbtString.of(Text.Serializer.toJson(new LiteralText(
                        "§2⚐ Category " + category.replaceFirst("&", "§")
                    ))));

                    if (rank == 0) {
                        rank = 1;
                    }
                    if (plotsize == 0) {
                        plotsize = 1;
                    }

                    loreTag.add(NbtString.of(Text.Serializer.toJson(new LiteralText(
                        "§5☐ §7" + ranks[rank - 1] +
                            " §5§l! §7" + plots[plotsize - 1]
                    ))));

                    if (!Objects.equals(lore, "")) {
                        loreTag.add(NbtString.of(
                            Text.Serializer.toJson(new LiteralText("§fDescription:"))));
                        for (String line : lore.split("\n")) {
                            loreTag.add(
                                    NbtString.of(Text.Serializer.toJson(new LiteralText(line))));
                        }
                    }

                    item.getSubNbt("display").put("Lore", loreTag);
                    item.setSubNbt("HideFlags", NbtInt.of(127));
                    item.setSubNbt("category", NbtString.of(categoryConv.get(category).toLowerCase()));

                    NbtCompound publicBukkitVals = item.getOrCreateSubNbt("PublicBukkitValues");
                    publicBukkitVals.putString("hypercube:codetemplatedata",templatedata);
                    items.add(item);
                } catch (Exception err) {
                    err.printStackTrace();
                }
            }
            CodeUtilities.MC.submit(this::update);
        });
    }
}
