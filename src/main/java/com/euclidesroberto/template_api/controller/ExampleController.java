package com.euclidesroberto.template_api.controller;

import com.euclidesroberto.template_api.dto.ExampleDto;
import com.euclidesroberto.template_api.model.Example;
import com.euclidesroberto.template_api.service.ExampleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/examples")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class ExampleController {

    private final ExampleService exampleService;

    @GetMapping
    @Operation(summary = "Get all examples", description = "Retrieve all examples")
    public ResponseEntity<List<Example>> findAll() {
        return ResponseEntity.ok(exampleService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get example by ID", description = "Retrieve a specific example by its ID")
    public ResponseEntity<Example> findById(@PathVariable Long id) {
        return ResponseEntity.ok(exampleService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('admin')")
    @Operation(summary = "Create a new example", description = "Create a new example (requires admin role)")
    public ResponseEntity<Example> create(@Valid @RequestBody ExampleDto exampleDto) {
        Example created = exampleService.create(exampleDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('admin')")
    @Operation(summary = "Update an example", description = "Update an existing example (requires admin role)")
    public ResponseEntity<Example> update(@PathVariable Long id, @Valid @RequestBody ExampleDto exampleDto) {
        Example updated = exampleService.update(id, exampleDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('admin')")
    @Operation(summary = "Delete an example", description = "Delete an example by its ID (requires admin role)")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        exampleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
