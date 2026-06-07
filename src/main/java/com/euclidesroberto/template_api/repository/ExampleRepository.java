package com.euclidesroberto.template_api.repository;

import com.euclidesroberto.template_api.model.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExampleRepository extends JpaRepository<Example, Long> {
    Optional<Example> findByName(String name);
    boolean existsByName(String name);
}
