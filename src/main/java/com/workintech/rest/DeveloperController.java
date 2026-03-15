package com.workintech.controller;

import com.workintech.model.Developer;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/developers")
public class DeveloperController {

    private List<Developer> developers = new ArrayList<>();

    // GET /developers → tüm developerları listeler
    @GetMapping
    public List<Developer> getAllDevelopers() {
        return developers;
    }

    // POST /developers → yeni developer ekler
    @PostMapping
    public Developer addDeveloper(@RequestBody Developer developer) {
        developers.add(developer);
        return developer;
    }
}