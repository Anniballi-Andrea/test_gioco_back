package test_gioco.demo.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import test_gioco.demo.classes.GameState;
import test_gioco.demo.classes.map.MapGrid;
import test_gioco.demo.classes.map.Portal;
import test_gioco.demo.classes.units.Unit;
import test_gioco.demo.dtos.AttackRequest;
import test_gioco.demo.dtos.CreateUnitRequest;
import test_gioco.demo.dtos.MiningRequest;
import test_gioco.demo.dtos.MoveRequest;
import test_gioco.demo.exeptions.SpawnException;
import test_gioco.demo.services.CombatService;
import test_gioco.demo.services.MapGeneratorService;
import test_gioco.demo.services.MiningService;
import test_gioco.demo.services.TurnService;
import test_gioco.demo.services.UnitService;

@RestController
@RequestMapping("api/game")
@CrossOrigin(origins = "http://localhost:5173/")
public class GameRestController {
    private final MapGeneratorService mapGeneratorService;
    private final UnitService unitService;
    private final CombatService combatService;
    private final TurnService turnService;
    private final MiningService miningService;

    private GameState gameState;

    public GameRestController(MapGeneratorService mapGeneratorService,
            UnitService unitService, CombatService combatService, TurnService turnService,
            MiningService miningService) {
        this.mapGeneratorService = mapGeneratorService;
        this.unitService = unitService;
        this.combatService = combatService;
        this.turnService = turnService;
        this.miningService = miningService;
    }

    @PostMapping("/start")
    public ResponseEntity<GameState> startGame() {
        MapGrid newMap = mapGeneratorService.generate();

        this.gameState = new GameState(newMap);

        Portal portal = mapGeneratorService.generatePortal(newMap);

        this.gameState.setPortal(portal);

        return new ResponseEntity<>(gameState, HttpStatus.OK);
    }

    @GetMapping("/state")
    public ResponseEntity<GameState> getGameState() {
        if (gameState == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(gameState, HttpStatus.OK);
    }

    @PostMapping("/units")
    public ResponseEntity<?> spawnUnit(@RequestBody CreateUnitRequest dto) {

        if (gameState == null) {
            return ResponseEntity.badRequest().build();
        }

        try {
            unitService.createUnit(gameState, dto.getType(), dto.getX(), dto.getY());
            return ResponseEntity.ok(gameState);

        } catch (SpawnException e) {
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    @PostMapping("/move")
    public ResponseEntity<GameState> moveUnit(@RequestBody MoveRequest dto) {
        if (gameState == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        boolean success = unitService.moveUnit(gameState, dto.getUnitId(), dto.getX(), dto.getY());
        if (!success) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(gameState, HttpStatus.OK);
    }

    @PostMapping("/attack")
    public ResponseEntity<GameState> attackUnit(@RequestBody AttackRequest dto) {
        if (gameState == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Unit attacker = unitService.findUnit(gameState, dto.getAttackerId());
        Unit target = unitService.findUnit(gameState, dto.getTargetId());

        if (attacker == null || target == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        combatService.attack(gameState, attacker, target);
        return new ResponseEntity<>(gameState, HttpStatus.OK);
    }

    @PostMapping("/extract")
    public ResponseEntity<GameState> extractResource(@RequestBody MiningRequest dto) {
        if (gameState == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Unit unit = unitService.findUnit(gameState, dto.getUnitId());
        if (unit == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Boolean success = miningService.extractResource(unit, dto.getTargetY(), dto.getTargetX(), gameState);

        if (!success) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(gameState, HttpStatus.OK);

    }

    @PostMapping("/end-turn")
    public ResponseEntity<GameState> endTurn() {
        if (gameState == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        turnService.endTurn(gameState);
        return new ResponseEntity<>(gameState, HttpStatus.OK);
    }

}
