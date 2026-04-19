package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.TaskRequest;
import org.example.model.Task;
import org.example.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = TaskController.class)
public class TaskControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TaskRepository repository;

    @Test
    public void create_validRequest_returnsCreated() throws Exception {
        Task toSave = new Task("Buy milk", "2 liters");
        // repository should set id when saving
        when(repository.save(any(Task.class))).thenAnswer(invocation -> {
            Task t = invocation.getArgument(0);
            t.setId(1L);
            return t;
        });

        TaskRequest req = new TaskRequest("Buy milk", "2 liters");

        mockMvc.perform(post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/tasks/1"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Buy milk"));
    }

    @Test
    public void create_blankTitle_returnsBadRequest() throws Exception {
        TaskRequest req = new TaskRequest("", "desc");

        mockMvc.perform(post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.title").exists());
    }

    @Test
    public void getById_notFound_returns404() throws Exception {
        when(repository.findById(42L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/tasks/42"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getById_found_returns200() throws Exception {
        Task t = new Task("Test","d");
        t.setId(5L);
        when(repository.findById(5L)).thenReturn(Optional.of(t));

        mockMvc.perform(get("/tasks/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.title").value("Test"));
    }
}
