package com.example.gamelistservice.controller;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.gamelistservice.model.Game;
import com.example.gamelistservice.repository.GameRepository;

@RestController
class GameController {
    private GameRepository repository;

    public GameController(GameRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/gamelist")
    @CrossOrigin(origins = "http://localhost:4200")
    public Collection<Game> gameList() {
        return repository.findAll().stream()
                .collect(Collectors.toList());
    }

}