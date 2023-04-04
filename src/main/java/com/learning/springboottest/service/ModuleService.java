package com.learning.springboottest.service;

import com.learning.springboottest.model.Module;

import java.util.List;

public interface ModuleService {


    Module saveModule(Module module);

    List<Module> getAllModules();

}
