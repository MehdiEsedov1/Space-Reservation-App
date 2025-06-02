package org.example.persistence;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.entity.Reservation;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class ReservationFileStorage {
    private static final String FILE_PATH = "reservations.json";
    private static final Gson gson = new Gson();

    public static void save(List<Reservation> reservations) throws IOException {
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            gson.toJson(reservations, writer);
        }
    }

    public static List<Reservation> load() throws IOException {
        try (FileReader reader = new FileReader(FILE_PATH)) {
            Type listType = new TypeToken<List<Reservation>>() {}.getType();
            return gson.fromJson(reader, listType);
        }
    }
}
