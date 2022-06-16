package io.github.codeutilities.screen.commands.codeutilities;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.screen.CScreen;
import io.github.codeutilities.screen.widget.CImage;
import io.github.codeutilities.screen.widget.CPlainPanel;
import io.github.codeutilities.screen.widget.CScrollPanel;
import io.github.codeutilities.screen.widget.CText;
import io.github.codeutilities.util.WebUtil;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.Level;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ContributorsScreen extends CScreen {
    private static ContributorsScreen INSTANCE;
    private final List<Contributor> contributors = new ArrayList<>();

    public static ContributorsScreen getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ContributorsScreen();
        }
        return INSTANCE;
    }

    private ContributorsScreen(String... args) {
        super(165, 110);
        CPlainPanel root = new CPlainPanel(0, 0, 165, 110);


        root.add(new CText(5, 3, new LiteralText("Contributors"), 0x333333, 1.25f, false, false));

        CPlainPanel panel = new CPlainPanel(0, 10, 165, 95);

        CScrollPanel scrollPanel = new CScrollPanel(0, 0, 165, 95);



        int y = 0;
        int x = 5;

        JsonArray array = WebUtil.getJSON("https://api.github.com/repos/CodeUtilities/CodeUtilities/contributors").getAsJsonArray();
        for (JsonElement element : array) {
            JsonObject object = element.getAsJsonObject();
            this.contributors.add(new Contributor(object.get("login").getAsString(), object.get("id").getAsInt(), object.get("contributions").getAsInt(), object.get("avatar_url").getAsString()));
        }

        for (Contributor contributor : contributors) {

            if (contributor.getAvatar() == null) {
                try {
                    URL url = new URL(contributor.getAvatarUrl());
                    Identifier identifier = CodeUtilities.MC.getTextureManager().registerDynamicTexture("contributor_" + contributor.getName().toLowerCase(), new NativeImageBackedTexture(NativeImage.read(url.openStream())));
                    contributor.setAvatar(identifier);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            CImage image = new CImage(x, y, 16, 16, contributor.getAvatar().toString());

            scrollPanel.add(image);
            scrollPanel.add(new CText(x + 20, y + 6, Text.of(contributor.getName())));

            CodeUtilities.LOGGER.log(Level.WARN, contributor.getName() + ": (" + x + ", " + y + ")");

            if (x == 75) {
                x = 5;
                y += 20;
            } else {
                x = 75;
            }
        }

        panel.add(scrollPanel);

        root.add(panel);

        widgets.add(root);

    }

    @Override
    public void close() {
        CodeUtilities.MC.setScreen(new CodeUtilitiesScreen());
    }
}