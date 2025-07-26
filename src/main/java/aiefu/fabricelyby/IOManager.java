package aiefu.fabricelyby;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class IOManager {

    public static void genCfg() throws IOException {
        String path = "./config/ely-by-for-fabric/config.json";
        if(!Files.exists(Paths.get(path))) {
            try(FileWriter writer = new FileWriter(path)){
                new GsonBuilder().setPrettyPrinting().create().toJson(new Config(), writer);
            }
        }
    }

    public static Config readCfg() throws FileNotFoundException {
        return new Gson().fromJson(new FileReader("./config/ely-by-for-fabric/config.json"), Config.class);
    }


    public static void craftPaths() throws IOException {
        Path dir = Paths.get("./config/ely-by-for-fabric");
        if(!Files.isDirectory(dir)){
            Files.createDirectories(dir);
        }
    }
}
