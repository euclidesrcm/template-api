package com.euclidesroberto.template_api.service;

import com.euclidesroberto.template_api.dto.ExampleDto;
import com.euclidesroberto.template_api.exception.ResourceNotFoundException;
import com.euclidesroberto.template_api.model.Example;
import com.euclidesroberto.template_api.repository.ExampleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ExampleService {

    private final ExampleRepository exampleRepository;

    public List<Example> findAll() {
        return exampleRepository.findAll();
    }

    public Example findById(Long id) {
        return exampleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Example not found with id: " + id));
    }

    public Example create(ExampleDto exampleDto) {
        if (exampleRepository.existsByName(exampleDto.getName())) {
            throw new IllegalArgumentException("Example with name '" + exampleDto.getName() + "' already exists");
        }

        Example example = Example.builder()
                .name(exampleDto.getName())
                .description(exampleDto.getDescription())
                .build();

        return exampleRepository.save(example);
    }

    public Example update(Long id, ExampleDto exampleDto) {
        Example existingExample = findById(id);

        if (!existingExample.getName().equals(exampleDto.getName()) && 
            exampleRepository.existsByName(exampleDto.getName())) {
            throw new IllegalArgumentException("Example with name '" + exampleDto.getName() + "' already exists");
        }

        existingExample.setName(exampleDto.getName());
        existingExample.setDescription(exampleDto.getDescription());

        return exampleRepository.save(existingExample);
    }

    public void delete(Long id) {
        Example example = findById(id);
        exampleRepository.delete(example);
    }
}
