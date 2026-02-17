package com.example.hackthon.entity;
import jakarta.persistence.*;


@Entity
@Table(name = "team_member")
public class TeamMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String role;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    // getters & setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public Team getTeam() { return team; }
    public void setTeam(Team team) { this.team = team; }
}
