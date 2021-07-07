package com.github.itsclairton.simplespawn.data;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;

public class DataManager {

    public static void saveToFile(File file, Object obj) {

        try {
            if(!file.exists()) file.createNewFile();

            BufferedWriter writer = Files.newWriter(file, Charsets.UTF_8);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            writer.write(gson.toJson(obj));
            writer.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    public static <T> T loadFromFile(File file, Class<T> clazz) {
        if(!file.exists()) return null;

        try {
            return new Gson().fromJson(String.join("\n", Files.readLines(file, Charsets.UTF_8)), clazz);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

}
