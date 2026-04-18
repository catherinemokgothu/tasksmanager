package org.example.controller;

import org.example.model.Task;
import org.example.repository.TaskRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskRepository repository;

    public TaskController(TaskRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Task> getAll() {
        return repository.findAll();
    }

    @PostMapping
    public ResponseEntity<Task> create(@RequestBody Task task) {
        Task saved = repository.save(task);
        return ResponseEntity.created(URI.create("/tasks/" + saved.getId())).body(saved);
    }
}
