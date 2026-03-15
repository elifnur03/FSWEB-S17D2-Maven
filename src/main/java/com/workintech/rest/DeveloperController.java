package com.workintech.rest;

import com.workintech.tax.DeveloperTax;
import org.springframework.web.bind.annotation.*;
import com.workintech.model.Developer;
import com.workintech.model.Experience;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/workintech/developers")
public class DeveloperController {

    public Map<Integer, Developer> developers = new HashMap<>();

    public DeveloperController(DeveloperTax developerTax) {
        developers.put(1, new Developer(1, "Alice", 5000, Experience.JUNIOR));
        developers.put(2, new Developer(2, "Bob", 8000, Experience.MID));
    }

    @GetMapping
    public Map<Integer, Developer> getAll() {
        return developers;
    }

    @GetMapping("/{id}")
    public Developer getById(@PathVariable int id) {
        return developers.get(id);
    }

    public void init() {
    }
}