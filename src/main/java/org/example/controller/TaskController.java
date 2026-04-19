package org.example.controller;

import org.example.dto.TaskRequest;
import org.example.dto.TaskResponse;
import org.example.model.Task;
import org.example.repository.TaskRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskRepository repository;

    public TaskController(TaskRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<TaskResponse> getAll() {
        return repository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<TaskResponse> create(@Valid @RequestBody TaskRequest taskReq) {
        Task task = new Task(taskReq.getTitle(), taskReq.getDescription());
        Task saved = repository.save(task);
        return ResponseEntity.created(URI.create("/tasks/" + saved.getId())).body(toResponse(saved));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getById(@PathVariable Long id) {
        return repository.findById(id)
                .map(task -> ResponseEntity.ok(toResponse(task)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Update an existing task
    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> update(@PathVariable Long id, @Valid @RequestBody TaskRequest taskReq) {
        return repository.findById(id)
                .map(existing -> {
                    // update fields
                    existing.setTitle(taskReq.getTitle());
                    existing.setDescription(taskReq.getDescription());
                    Task updated = repository.save(existing);
                    return ResponseEntity.ok(toResponse(updated));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Delete a task
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return repository.findById(id)
                .map(existing -> {
                    repository.deleteById(id);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    private TaskResponse toResponse(Task t) {
        return new TaskResponse(t.getId(), t.getTitle(), t.getDescription());
    }
}
