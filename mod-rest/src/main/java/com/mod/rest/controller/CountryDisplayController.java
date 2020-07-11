package com.mod.rest.controller;

import com.mod.rest.service.CountryDisplayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/country-display")
public class CountryDisplayController {

    @Autowired
    CountryDisplayService countryDisplayService;

    @GetMapping("pdf/{id}")
    @ResponseBody
    public ResponseEntity<byte[]> generatePDF(@PathVariable long id) {
        return countryDisplayService.generatePDF(id);
    }
}
