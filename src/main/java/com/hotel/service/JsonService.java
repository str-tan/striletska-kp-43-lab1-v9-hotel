package com.hotel.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class JsonService {
    private final ObjectMapper mapper;

    public JsonService() {
        this.mapper = new ObjectMapper();
        //JSON з відступами
        this.mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    //Експорт даних у файл
    public <T> void saveToFile(String fileName, List<T> data) throws IOException {
        mapper.writeValue(new File(fileName), data);
    }

    //Імпорт даних із файлу
    public <T> List<T> loadFromFile(String fileName, Class<T> clazz) throws IOException {
        File file = new File(fileName);
        if (!file.exists()) return List.of();

        return mapper.readValue(file,
                mapper.getTypeFactory().constructCollectionType(List.class, clazz));
    }
}