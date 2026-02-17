package com.example.hackthon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.hackthon.entity.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
}
