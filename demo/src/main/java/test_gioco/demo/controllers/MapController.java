package test_gioco.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import test_gioco.demo.classes.map.MapGrid;
import test_gioco.demo.services.MapGeneratorService;

@Controller
public class MapController {

    private final MapGeneratorService service;

    public MapController(MapGeneratorService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String map(Model model) {
        MapGrid grid = service.generate();

        model.addAttribute("map", grid);

        return "map";
    }

}
