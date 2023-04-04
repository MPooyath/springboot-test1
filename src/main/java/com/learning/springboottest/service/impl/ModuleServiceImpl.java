package com.learning.springboottest.service.impl;

import com.learning.springboottest.model.Module;
import com.learning.springboottest.repository.ModuleRepository;
import com.learning.springboottest.service.ModuleService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModuleServiceImpl implements ModuleService {


    private ModuleRepository moduleRepository;

    public ModuleServiceImpl( ModuleRepository moduleRepository) {

        this.moduleRepository = moduleRepository;
    }

    @Override
    public Module saveModule(Module module) {
        return moduleRepository.save(module);
    }

    @Override
    public List<Module> getAllModules() {
        return moduleRepository.findAll();
    }


}
