package test_gioco.demo.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import test_gioco.demo.classes.map.MapGrid;
import test_gioco.demo.services.MapGeneratorService;

@RestController
@RequestMapping("/api/getmap")
@CrossOrigin(origins = "http://localhost:5173/")
public class MapRestController {

    private final MapGeneratorService service;

    public MapRestController(MapGeneratorService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<MapGrid> show() {
        MapGrid map = service.generate();

        return new ResponseEntity<MapGrid>(map, HttpStatus.OK);
    }
}
