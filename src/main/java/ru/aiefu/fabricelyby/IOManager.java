package ru.aiefu.fabricelyby;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class IOManager {

    public static void genCfg(){
        if(!Files.exists(Paths.get("./config/ely-by-for-fabric/config.json"))) {
            String gson = new GsonBuilder().setPrettyPrinting().create().toJson(new Config());
            File file = new File("./config/ely-by-for-fabric/config.json");
            fileWriter(file, gson);
        }
    }

    public static Config readCfg() throws FileNotFoundException {
        return new Gson().fromJson(new FileReader("./config/ely-by-for-fabric/config.json"), Config.class);
    }


    public static void craftPaths() throws IOException {
        if(!Files.isDirectory(Paths.get("./config/ely-by-for-fabric"))){
            Files.createDirectories(Paths.get("./plugins/ely-by-for-fabric"));
        }
    }

    public static void fileWriter(File file, String gson){
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try(FileWriter writer = new FileWriter(file)) {
            writer.write(gson);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
