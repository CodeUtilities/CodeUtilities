package io.github.codeutilities.screen.commands.codeutilities;

import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.screen.CScreen;
import io.github.codeutilities.screen.widget.*;
import io.github.codeutilities.util.chat.text.TextUtil;
import net.minecraft.text.LiteralText;

import java.util.List;

public class FeaturesScreen extends CScreen {

    private static final String CODEUTILS_LOGO = "codeutilities:icon.png";

    public FeaturesScreen() {
        super(100, 100);

        List<Feature> features = FeatureList.get();

        CWrappedText text = new CWrappedText(45, 5, 110,
                new LiteralText("Click a feature on the left to view its description"), 0x333333, 0.85f, false, false);

        CScrollPanel sBtnList = new CScrollPanel(0, 0, 50, 100);

        int y = 0;
        for (Feature f : features) {
            CButton btn = new CButton(0, y, 40, 10, 0.85f, f.getName(), () -> {
            });
            btn.setOnClick(() -> {
                //all.forEach(b -> b.setEnabled(true));
                //btn.setEnabled(false);

                String desc = f.getDescription();

                desc = desc.replaceAll("\\[red\\]", "§x§b§7§1§2§0§0"); //: "§x§d§d§4§6§2§c");
                desc = desc.replaceAll("\\[blue\\]", "§x§0§0§0§0§a§b"); //: "§x§6§8§a§e§e§3");
                desc = desc.replaceAll("\\[green\\]", "§x§0§0§8§7§0§f"); //: "§x§6§8§a§e§e§3");
                desc = desc.replaceAll("\\[yellow\\]", "§x§d§3§8§2§0§0"); //: "§x§8§1§e§2§4§2");
                desc = desc.replaceAll("\\[reset\\]", "§r");

                text.setText(TextUtil.colorCodesToTextComponent(desc));
            });
            sBtnList.add(btn);

            y += 10;
        }

        widgets.add(sBtnList);
        widgets.add(text);
    }

    @Override
    public void close() {
        CodeUtilities.MC.setScreen(new CodeUtilitiesScreen());
    }

//    @Override
//    public void open(String... args) {
//        WPlainPanel root = new WPlainPanel();
//        root.setSize(425, 220);
//
//        WPlainPanel panel = new WPlainPanel();
//        WScrollPanel scrollPanel = new WScrollPanel(panel);
//        scrollPanel.setHost(this);
//
//        root.add(scrollPanel, 0, 0, 425, 220);
//        panel.add(new WLabel(new LiteralText("CodeUtilities Features || v" + CodeUtilities.MOD_VERSION)), 4, 4);
//
//        CImage cImage = new CImage(CODEUTILS_LOGO);
//        cImage.setSize(60, 60);
//        panel.add(cImage, 355, 0);
//
//        try {
//            URL oracle = new URL("https://raw.githubusercontent.com/CodeUtilities/data/main/menus/featuresGUI-" + CodeUtilities.MOD_VERSION + ".txt");
//            BufferedReader in = new BufferedReader(
//                    new InputStreamReader(oracle.openStream()));
//
//            String inputLine;
//            int y = 14;
//
//            while ((inputLine = in.readLine()) != null) {
//                y += 10;
//                if (inputLine.contains("-")) {
//                    String[] inls = inputLine.replace("§l/§r", "/").split("-");
//                    String[] colors = {"§x§C§C§5§D§0§0", "§x§3§3§0§3§0§0", "§x§C§C§5§2§0§0"};
//                    Text txt = TextUtil.colorCodesToTextComponent(colors[0]+inls[0]+colors[1]+" - "+colors[2]+inls[1]);
//                    panel.add(new WLabel(txt), 4, y);
//                }
//            }
//
//            in.close();
//
//        } catch (Exception e) {
//            CodeUtilities.log(Level.ERROR, String.valueOf(e));
//        }
//
//        panel.setHost(this);
//        setRootPanel(root);
//    }
}
