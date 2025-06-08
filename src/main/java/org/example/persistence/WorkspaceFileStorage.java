package org.example.persistence;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.entity.Workspace;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class WorkspaceFileStorage {
    private static final String FILE_PATH = "workspaces.json";
    private static final Gson gson = new Gson();

    public static void save(List<Workspace> workspaces) throws IOException {
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            gson.toJson(workspaces, writer);
        }
    }

    public static List<Workspace> load() throws IOException {
        try (FileReader reader = new FileReader(FILE_PATH)) {
            Type listType = new TypeToken<List<Workspace>>() {}.getType();
            return gson.fromJson(reader, listType);
        }
    }
}
