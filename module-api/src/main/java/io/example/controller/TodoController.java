package io.example.controller;

import io.example.entity.Todo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/todos")
public class TodoController {

    public static List<Todo> storege = Collections.synchronizedList(new ArrayList<Todo>());

    @GetMapping
    public ResponseEntity<?> todos() {
        log.debug(">>> todos: {}", storege);
        return ResponseEntity.status(HttpStatus.OK).body(storege);
    }

    @GetMapping("/{task}")
    public ResponseEntity<?> todo(@PathVariable("task") String task) {
        log.debug(">>> todo: {}", task);
        Todo result = findByTask(task);

        if (Objects.isNull(result)) {
            return notFound();
        }

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PostMapping
    public ResponseEntity<?> add(@RequestBody Todo todo) {
        storege.add(todo);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{task}")
    public ResponseEntity<?> update(@PathVariable("task") String task,
                                    @RequestBody Todo newTask) {
        Todo result = findByTask(task);

        if (Objects.isNull(result)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        storege.remove(result);
        storege.add(newTask);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{task}")
    public ResponseEntity<?> delete(@PathVariable("task") String task) {
        Todo result = findByTask(task);

        if (Objects.isNull(result)) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        storege.remove(result);

        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    private Todo findByTask(String task) {
        Todo result = storege.stream().filter(o -> o.getTask().equals(task)).findAny().orElse(null);
        return result;
    }

    private ResponseEntity<?> notFound() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
