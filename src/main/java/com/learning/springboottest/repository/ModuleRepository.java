package com.learning.springboottest.repository;

import com.learning.springboottest.model.Module;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModuleRepository extends JpaRepository<Module,Long> {
}
