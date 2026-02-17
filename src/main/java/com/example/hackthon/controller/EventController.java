package com.example.hackthon.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.hackthon.entity.Event;
import com.example.hackthon.repository.EventRepository;

@RestController
@RequestMapping("/api/events")
@CrossOrigin(origins = "http://localhost:4200")
public class EventController {

    @Autowired
    private EventRepository eventRepo;

    // CREATE EVENT (Admin)
    @PostMapping
    public Event createEvent(@RequestBody Event event) {
        System.out.println("EVENT DATA: " + event.getTitle());
        return eventRepo.save(event);
    }

    // VIEW ALL EVENTS (Student)
    @GetMapping
    public List<Event> getAllEvents() {
        return eventRepo.findAll();
    }
}
