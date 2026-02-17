package com.example.hackthon.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.hackthon.entity.Team;
import com.example.hackthon.repository.TeamRepository;

@RestController
@RequestMapping("/api/teams")
@CrossOrigin(origins = "http://localhost:4200")
public class TeamController {

    @Autowired
    private TeamRepository repo;

    @PostMapping
    public Team registerTeam(@RequestBody Team team) {
        return repo.save(team);
    }

    @GetMapping
    public List<Team> getAllTeams() {
        return repo.findAll();
    }
}
