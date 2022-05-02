package io.github.codeutilities.websocket.client;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.websocket.SocketHandler;
import io.github.codeutilities.websocket.client.type.SocketItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.sound.SoundEvents;
import io.github.codeutilities.util.ItemUtil;
import io.github.codeutilities.util.RenderUtil;

@Environment(EnvType.CLIENT)
public class Clients {

    public static String acceptData(String line) {
        JsonObject result = new JsonObject();
        try {
            if (line == null) {
                return null;
            }
            MinecraftClient mc = CodeUtilities.MC;

            JsonObject data = JsonParser.parseString(line).getAsJsonObject();
            String type = data.get("type").getAsString();
            String itemData = data.get("data").getAsString();
            String source = data.get("source").getAsString();

            SocketItem item = SocketHandler.getInstance().getSocketItems().get(type);
            if (item == null) {
                throw new IllegalArgumentException("Could not find an item type that matched " + type + "!");
            }
            if (mc.player == null) {
                throw new Exception("Player is not logged in!");
            }

            if (mc.player.isCreative()) {
                ItemUtil.giveCreativeItem(item.getItem(itemData), true);
                RenderUtil.sendToaster("Received Item!", source, SystemToast.Type.NARRATOR_TOGGLE);
                result.addProperty("status", "success");
            } else {
                throw new Exception("Player is not in creative!");
            }
        } catch (Throwable e) {
            result.addProperty("status", "error");
            result.addProperty("error", e.getMessage());
        }

        return result.toString();
    }

}