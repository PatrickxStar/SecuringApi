package com.example.securingapi.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class DetailsController {

    @GetMapping("/details")
    public String getDetails() {
        return "This is a protected endpoint!";
    }
}
