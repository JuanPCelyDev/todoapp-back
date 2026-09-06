package com.pruebatecnica.todoapp.service;



import com.pruebatecnica.todoapp.dto.request.CreateTaskRequest;
import com.pruebatecnica.todoapp.dto.request.UpdateTaskRequest;
import com.pruebatecnica.todoapp.dto.response.TaskResponse;
import com.pruebatecnica.todoapp.entity.Task;
import com.pruebatecnica.todoapp.entity.User;
import com.pruebatecnica.todoapp.entity.enums.TaskStatus;
import com.pruebatecnica.todoapp.exception.ResourceNotFoundException;
import com.pruebatecnica.todoapp.repository.TaskRepository;
import com.pruebatecnica.todoapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskResponse createTask(CreateTaskRequest request, String userEmail) {
        User user = getUserByEmail(userEmail);

        Task task = Task.builder()
                .title(request.title())
                .description(request.description())
                .dueDate(request.dueDate())
                .creationDate(LocalDateTime.now())
                .status(TaskStatus.PENDING)
                .user(user)
                .build();

        Task saved = taskRepository.save(task);
        return toResponse(saved);
    }

    public List<TaskResponse> getTasksByUser(String userEmail, TaskStatus status) {
        User user = getUserByEmail(userEmail);

        List<Task> tasks = (status != null)
                ? taskRepository.findByStatusAndUserId(status, user.getId())
                : taskRepository.findAllByUserId(user.getId());

        return tasks.stream().map(this::toResponse).toList();
    }

    public TaskResponse updateTask(UUID taskId, UpdateTaskRequest request, String userEmail) {
        Task task = getTaskOwnedByUser(taskId, userEmail);

        if (request.title() != null) task.setTitle(request.title());
        if (request.description() != null) task.setDescription(request.description());
        if (request.dueDate() != null) task.setDueDate(request.dueDate());
        if (request.status() != null) task.setStatus(request.status());

        Task updated = taskRepository.save(task);
        return toResponse(updated);
    }

    public void deleteTask(UUID taskId, String userEmail) {
        Task task = getTaskOwnedByUser(taskId, userEmail);
        taskRepository.delete(task);
    }

    private Task getTaskOwnedByUser(UUID taskId, String userEmail) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Tarea no encontrada con id: " + taskId));

        if (!task.getUser().getEmail().equals(userEmail)) {
            throw new ResourceNotFoundException("Tarea no encontrada con id: " + taskId);
        }

        return task;
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
    }

    private TaskResponse toResponse(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getCreationDate(),
                task.getDueDate(),
                task.getStatus()
        );
    }
}
