package org.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestController
public class InfoController {

    @GetMapping("/healthcheck")
    public Map<String, Object> health() {
        Map<String, Object> m = new HashMap<>();
        m.put("status", "Application is running");
        m.put("timestamp", Instant.now().toString());
        return m;
    }
}
