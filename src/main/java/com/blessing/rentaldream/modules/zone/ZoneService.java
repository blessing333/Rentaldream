package com.blessing.rentaldream.modules.zone;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ZoneService {
    private final ZoneRepository zoneRepository;
    private final ObjectMapper objectMapper;

    @PostConstruct
    public void init() throws IOException {
        if(zoneRepository.count() == 0) {
            Resource resource = new ClassPathResource("static/zones_kr.csv");
            InputStream inputStream = resource.getInputStream();
            try(BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))){
                List<Zone> zoneList = Files.readAllLines(resource.getFile().toPath(), StandardCharsets.UTF_8)
                        .stream()
                        .map(line -> {
                            String[] split = line.split(",");
                            return Zone.builder()
                                    .city(split[0])
                                    .localCityName(split[1])
                                    .province(split[2])
                                    .build();
                        }).collect(Collectors.toList());
                zoneRepository.saveAll(zoneList);
            }
        }
    }

    public Zone findByCityAndProvince(String city, String province) {
        return zoneRepository.findByCityAndProvince(city,province);
    }

    public String findAllZonesAsJsonString() throws JsonProcessingException {
        List<String> allZones = zoneRepository.findAll().stream().map(Zone::toString).collect(Collectors.toList());
        return objectMapper.writeValueAsString(allZones);

    }
}
