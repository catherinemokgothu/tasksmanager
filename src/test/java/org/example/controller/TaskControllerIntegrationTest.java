package org.example.controller;

import org.example.dto.TaskRequest;
import org.example.dto.TaskResponse;
import org.example.model.Task;
import org.example.repository.TaskRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TaskControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TaskRepository repository;

    @Test
    public void createAndGetAndUpdateAndDelete_flow() {
        String base = "http://localhost:" + port + "/tasks";

        // create
        TaskRequest req = new TaskRequest("Integrate","it");
        ResponseEntity<TaskResponse> created = restTemplate.postForEntity(base, req, TaskResponse.class);
        Assertions.assertEquals(HttpStatus.CREATED, created.getStatusCode());
        TaskResponse body = created.getBody();
        Assertions.assertNotNull(body);
        Long id = body.getId();

        // get
        ResponseEntity<TaskResponse> getResp = restTemplate.getForEntity(base + "/" + id, TaskResponse.class);
        Assertions.assertEquals(HttpStatus.OK, getResp.getStatusCode());
        Assertions.assertEquals("Integrate", getResp.getBody().getTitle());

        // update
        TaskRequest updateReq = new TaskRequest("Integrated","done");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<TaskRequest> entity = new HttpEntity<>(updateReq, headers);
        ResponseEntity<TaskResponse> updated = restTemplate.exchange(base + "/" + id, HttpMethod.PUT, entity, TaskResponse.class);
        Assertions.assertEquals(HttpStatus.OK, updated.getStatusCode());
        Assertions.assertEquals("Integrated", updated.getBody().getTitle());

        // delete
        ResponseEntity<Void> deleted = restTemplate.exchange(base + "/" + id, HttpMethod.DELETE, null, Void.class);
        Assertions.assertEquals(HttpStatus.NO_CONTENT, deleted.getStatusCode());

        // ensure deleted
        ResponseEntity<TaskResponse> notFound = restTemplate.getForEntity(base + "/" + id, TaskResponse.class);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, notFound.getStatusCode());
    }
}
