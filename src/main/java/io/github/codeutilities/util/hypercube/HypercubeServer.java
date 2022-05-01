package io.github.codeutilities.util.hypercube;

import io.github.codeutilities.CodeUtilities;
import net.minecraft.client.gui.screen.ConnectScreen;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;

public enum HypercubeServer {
    PROXY(null, ""),
    NODE_BETA("beta", " Beta"),
    NODE_1("node1", " Node1"),
    NODE_2("node2", " Node2"),
    NODE_3("node3", " Node3"),
    NODE_4("node4", " Node4"),
    NODE_5("node5", " Node5"),
    NODE_6("node6", " Node6"),
    NODE_7("node7", " Node7");

    private final String ext;
    private final String name;

    HypercubeServer(String ext, String name) {
        this.ext = ext;
        this.name = name;
    }

    public String getExt() {
        return ext;
    }

    public String getName() {
        return name;
    }

    public void connect() {
        String address = "mcdiamondfire.com";
        if (ext != null) {
            address = ext + "." + address;
        }

        ServerAddress serverAddress = new ServerAddress(address, 25565);
        ServerInfo serverInfo = new ServerInfo("DF" + name, address + ":25565", false);

        ConnectScreen.connect(CodeUtilities.MC.currentScreen, CodeUtilities.MC, serverAddress, serverInfo);
    }
}
