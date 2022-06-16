package io.github.codeutilities.screen.commands.nbs;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.features.tab.CodeUtilitiesServer;
import io.github.codeutilities.screen.CScreen;
import io.github.codeutilities.screen.widget.CButton;
import io.github.codeutilities.screen.widget.CScrollPanel;
import io.github.codeutilities.screen.widget.CText;
import io.github.codeutilities.util.ItemUtil;
import io.github.codeutilities.util.nbs.NBSToTemplate;
import io.github.codeutilities.util.nbs.SongData;
import io.github.codeutilities.util.template.TemplateUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.LiteralText;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class NbsSearchPanel extends CScreen {

    private final SoundEvent[] instrumentids = {
        SoundEvents.BLOCK_NOTE_BLOCK_HARP,
        SoundEvents.BLOCK_NOTE_BLOCK_BASS,
        SoundEvents.BLOCK_NOTE_BLOCK_BASEDRUM,
        SoundEvents.BLOCK_NOTE_BLOCK_SNARE,
        SoundEvents.BLOCK_NOTE_BLOCK_HAT,
        SoundEvents.BLOCK_NOTE_BLOCK_GUITAR,
        SoundEvents.BLOCK_NOTE_BLOCK_FLUTE,
        SoundEvents.BLOCK_NOTE_BLOCK_BELL,
        SoundEvents.BLOCK_NOTE_BLOCK_CHIME,
        SoundEvents.BLOCK_NOTE_BLOCK_XYLOPHONE,
        SoundEvents.BLOCK_NOTE_BLOCK_IRON_XYLOPHONE,
        SoundEvents.BLOCK_NOTE_BLOCK_COW_BELL,
        SoundEvents.BLOCK_NOTE_BLOCK_DIDGERIDOO,
        SoundEvents.BLOCK_NOTE_BLOCK_BIT
    };
    int previewId = -1;

    public NbsSearchPanel(String query) {
        super(300, 240);
        //this.query = query;

        MinecraftClient mc = CodeUtilities.MC;

        CText queryField = new CText(0, 0, new LiteralText("§l§nSearch Results for: " + query));
        widgets.add(queryField);

        //WPlainPanel ppanel = new WPlainPanel();

        CText loading = new CText(85, 50, new LiteralText("§lLoading Results..."));
        widgets.add(loading);

        //ppanel.add(loading, 85, 50, 300, 0);

        CScrollPanel spanel = new CScrollPanel(0, 10, 300, 230);

        CodeUtilities.EXECUTOR
                .submit(() -> {
            String sresults = CodeUtilitiesServer.requestURL(
                "https://untitled-57qvszfgg28u.runkit.sh/search?query=" + URLEncoder
                    .encode(query, StandardCharsets.UTF_8));

            JsonArray results = CodeUtilities.JSON_PARSER.parse(sresults).getAsJsonArray();

            mc.execute(() -> {
                widgets.remove(loading);

                int y = 5;

                for (JsonElement resulte : results) {
                    JsonObject e = resulte.getAsJsonObject();

                    int id = e.get("id").getAsInt();
                    String duration = e.get("duration").getAsString();
                    CText title = new CText(0, y, new LiteralText(e.get("title").getAsString()));
                    CText description = new CText(0, y + 10,
                        new LiteralText(duration + " §8-§r " + e.get("composer").getAsString()));

                    widgets.add(title);
                    widgets.add(description);

                    CButton download = new CButton(270, y, 20, 20, new LiteralText("§l↓").asString(), () -> {});

                    download.setOnClick(() -> {

                        download.setText(new LiteralText("...").asString());
                        CodeUtilities.EXECUTOR
                                .submit(() -> {
                                String notes = CodeUtilitiesServer.requestURL(
                                    "https://untitled-57qvszfgg28u.runkit.sh/download?format=mcnbs&id="
                                        + id);
                                String[] notearr = notes.split("=");
                                int length = Integer
                                    .parseInt(notearr[notearr.length - 1].split(":")[0]);
                                SongData d = new SongData("Song " + id, "CodeUtilities", 20f,
                                    length, notes, "", "", 1, 0, 0);

                                String code = new NBSToTemplate(d).convert();

                                ItemStack stack = new ItemStack(Items.NOTE_BLOCK);
                                TemplateUtil
                                    .compressTemplateNBT(stack, d.getName(), d.getAuthor(), code);

                                stack.setCustomName(
                                    new LiteralText("§d" + e.get("title").getAsString()));

                                ItemUtil.giveCreativeItem(stack, true);
                                download.setText(new LiteralText("§l↓").asString());
                            });
                    });

                    widgets.add(download);

                    CButton preview = new CButton(250, y, 20, 20, new LiteralText("▶").asString(), () -> {});

                    preview.setOnClick(() -> {
                        if (previewId != id) {
                            previewId = id;
                            preview.setText(new LiteralText("...").asString());
                            CodeUtilities.EXECUTOR
                                .submit(() -> {
                                    String snotes = CodeUtilitiesServer.requestURL(
                                        "https://untitled-57qvszfgg28u.runkit.sh/download?format=mcnbs&id="
                                            + id);
                                    List<String> notes = new ArrayList<>(
                                        Arrays.asList(snotes.split("=")));

                                    preview.setText(new LiteralText("■").asString());

                                    int[] tick = {0};
                                    int[] index = {0};
                                    ScheduledExecutorService scheduler = Executors
                                        .newScheduledThreadPool(1);
                                    scheduler.scheduleAtFixedRate(() -> CodeUtilities.MC.submit(() -> { //apparently playing sounds non-sync can crash the game
                                        if (previewId != id
                                            || mc.currentScreen == null) {
                                            scheduler.shutdown();
                                            preview.setText(new LiteralText("▶").asString());
                                            return;
                                        }
                                        if (notes.get(index[0])
                                            .startsWith(tick[0] + ":")) {

                                            String line = notes.get(index[0])
                                                .split(":")[1];

                                            for (String n : line.split(";")) {
                                                String[] d = n.split(",");
                                                int instrumentid = Integer
                                                    .parseInt(d[0]);
                                                float pitch =
                                                    (float) Integer.parseInt(d[1])
                                                        / 1000;
                                                float panning =
                                                    (float) Integer.parseInt(d[2]) / 100;
                                                mc.player
                                                    .playSound(
                                                        instrumentids[instrumentid - 1],
                                                        panning, pitch);
                                            }

                                            index[0]++;
                                        }
                                        tick[0]++;
                                    }), 0, 1000 / 20, TimeUnit.MILLISECONDS);
                                });
                        } else {
                            previewId = -1;
                        }
                    });

                    widgets.add(preview);

                    y += 25;
                }


            });
        });

        widgets.add(spanel);

    }
}
