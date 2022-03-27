package io.github.codeutilities.util;

import io.github.codeutilities.CodeUtilities;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileUtil {

    public static Path cuFolder() {
        return CodeUtilities.MC.runDirectory.toPath().resolve("CodeUtilities");
    }

    public static Path cuFolder(String path) {
        return cuFolder().resolve(path);
    }

    public static String readFile(Path path) throws IOException {
        return String.join("\n", Files.readAllLines(path));
    }

    public static void writeFile(Path path, String content) throws IOException {
        Files.writeString(path, content);
    }

}
