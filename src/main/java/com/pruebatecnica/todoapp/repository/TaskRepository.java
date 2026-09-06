package com.pruebatecnica.todoapp.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.pruebatecnica.todoapp.entity.Task;
import com.pruebatecnica.todoapp.entity.enums.TaskStatus;

public interface TaskRepository extends JpaRepository<Task, UUID> {
   
   List<Task> findAllByUserId(UUID userId);
   List<Task> findByStatusAndUserId(TaskStatus status, UUID userId);
   Page<Task> findAllByUserId(UUID userId, Pageable pageable);
}
