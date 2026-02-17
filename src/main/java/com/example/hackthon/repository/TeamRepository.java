package com.example.hackthon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.hackthon.entity.Team;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
