package com.game.controller;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class PlayerController {
    private PlayerService playerService;
    @Autowired
    public PlayerController(PlayerService playerService){
        this.playerService = playerService;
    }

    @GetMapping(value = "/rest/players")
    public ResponseEntity<List<Player>> getPlayersList(@RequestParam(required = false) String name,
                                                   @RequestParam(required = false) String title,
                                                   @RequestParam(required = false) Race race,
                                                   @RequestParam(required = false) Profession profession,
                                                   @RequestParam(required = false) Long after,
                                                   @RequestParam(required = false) Long before,
                                                   @RequestParam(required = false) Boolean banned,
                                                   @RequestParam(required = false) Integer minExperience,
                                                   @RequestParam(required = false) Integer maxExperience,
                                                   @RequestParam(required = false) Integer minLevel,
                                                   @RequestParam(required = false) Integer maxLevel,
                                                   @RequestParam(defaultValue = "ID") PlayerOrder order,
                                                   @RequestParam(defaultValue = "0") Integer pageNumber,
                                                   @RequestParam(defaultValue = "3") Integer pageSize){
        List<Player> players = playerService.getPlayersList(name, title, race, profession, after, before, banned,
                                        minExperience, maxExperience, minLevel, maxLevel, order, pageNumber, pageSize);

       return new ResponseEntity<>(players, HttpStatus.OK);


    }

    @GetMapping(value = "/rest/players/count")
    public ResponseEntity<Integer> getPlayersCount(@RequestParam(required = false) String name,
                                   @RequestParam(required = false) String title,
                                   @RequestParam(required = false) Race race,
                                   @RequestParam(required = false) Profession profession,
                                   @RequestParam(required = false) Long after,
                                   @RequestParam(required = false) Long before,
                                   @RequestParam(required = false) Boolean banned,
                                   @RequestParam(required = false) Integer minExperience,
                                   @RequestParam(required = false) Integer maxExperience,
                                   @RequestParam(required = false) Integer minLevel,
                                   @RequestParam(required = false) Integer maxLevel) {
        Integer playersCount = playerService.getPlayersCount(name, title, race, profession, after, before, banned, minExperience, maxExperience, minLevel, maxLevel);
        return new ResponseEntity<>(playersCount, HttpStatus.OK);
    }

    @PostMapping(value =  "/rest/players")
    //public ResponseEntity<Player> createPlayer(@RequestBody Map<String, String> map){
    public ResponseEntity<Player> createPlayer(@RequestBody Map<String, String> map){
        Player newPlayer = playerService.createPlayer(map);
        return new ResponseEntity<>(newPlayer, HttpStatus.OK);
    }

    @GetMapping(value = "/rest/players/{id}")
    public ResponseEntity<Player> getPlayer(@PathVariable String id){
        Player player = playerService.getPlayer(id);
        return new ResponseEntity<>(player, HttpStatus.OK);
    }

    @PostMapping (value = "/rest/players/{id}")
    public ResponseEntity<Player> updatePlayer(@RequestBody Map<String, String> map, @PathVariable String id){
        Player updatedPlayer = playerService.updatePlayer(map, id);
    return new ResponseEntity<>(updatedPlayer, HttpStatus.OK);
    }

    @DeleteMapping (value = "/rest/players/{id}")
    public ResponseEntity deletePlayer(@PathVariable String id){
        playerService.deletePlayer(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}
