package com.learning.springboottest.service.impl;

import com.learning.springboottest.model.Module;
import com.learning.springboottest.repository.ModuleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


public class ModuleServiceImplTests {

    @Mock
    private ModuleRepository moduleRepository;

    @InjectMocks
    private ModuleServiceImpl moduleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveModule() {
        // Arrange
        Module module = new Module(1l,"Intro",10);
        when(moduleRepository.save(module)).thenReturn(module);

        // Act
        Module savedModule = moduleService.saveModule(module);

        // Assert
        assertEquals(module, savedModule);
        verify(moduleRepository, times(1)).save(module);
    }

    @Test
    public void testGetAllModules() {
        // Arrange
        List<Module> modules = new ArrayList<>();
        Module module1 = new Module(1l,"Intro",10);
        Module module2 = new Module(2l,"Creating Project",10);
        modules.add(module1);
        modules.add(module2);
        when(moduleRepository.findAll()).thenReturn(modules);

        // Act
        List<Module> allModules = moduleService.getAllModules();

        // Assert
        assertEquals(modules, allModules);
        verify(moduleRepository, times(1)).findAll();
        assertThat(allModules).containsExactlyElementsOf(modules);
    }

}


