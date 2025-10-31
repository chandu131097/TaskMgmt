package com.example.taskapi.service;

import com.example.taskapi.dto.TaskRequest;
import com.example.taskapi.dto.TaskResponse;
import com.example.taskapi.entity.Task;
import com.example.taskapi.entity.TaskStatus;
import com.example.taskapi.exception.ResourceNotFoundException;
import com.example.taskapi.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TaskService {
    private final TaskRepository repo;
    public TaskService(TaskRepository repo) { this.repo = repo; }

    public TaskResponse createTask(TaskRequest req) {
        Task t = new Task();
        t.setTitle(req.getTitle());
        t.setDescription(req.getDescription());
        t.setDueDate(req.getDueDate());
        if (req.getStatus() != null) t.setStatus(TaskStatus.valueOf(req.getStatus()));
        t.setPriority(req.getPriority() != null ? req.getPriority() : 3);
        return toResponse(repo.save(t));
    }

    public TaskResponse getById(Long id) {
        Task t = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Task", "id", id));
        return toResponse(t);
    }

    public List<TaskResponse> getAll() {
        return repo.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    public TaskResponse update(Long id, TaskRequest req) {
        Task t = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Task", "id", id));
        t.setTitle(req.getTitle());
        t.setDescription(req.getDescription());
        t.setDueDate(req.getDueDate());
        if (req.getStatus() != null) t.setStatus(TaskStatus.valueOf(req.getStatus()));
        if (req.getPriority() != null) t.setPriority(req.getPriority());
        return toResponse(repo.save(t));
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) throw new ResourceNotFoundException("Task", "id", id);
        repo.deleteById(id);
    }

    private TaskResponse toResponse(Task t) {
        TaskResponse r = new TaskResponse();
        r.setId(t.getId());
        r.setTitle(t.getTitle());
        r.setDescription(t.getDescription());
        r.setDueDate(t.getDueDate());
        r.setStatus(t.getStatus());
        r.setPriority(t.getPriority());
        r.setCreatedAt(t.getCreatedAt());
        return r;
    }
}