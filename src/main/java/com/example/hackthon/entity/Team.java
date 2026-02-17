package com.example.hackthon.entity;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "team")
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String teamName;
    private String paymentMode;
    private String paymentStatus;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL)
    private List<TeamMember> members;

    // getters & setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTeamName() { return teamName; }
    public void setTeamName(String teamName) { this.teamName = teamName; }

    public String getPaymentMode() { return paymentMode; }
    public void setPaymentMode(String paymentMode) { this.paymentMode = paymentMode; }

    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }

    public Event getEvent() { return event; }
    public void setEvent(Event event) { this.event = event; }

    public List<TeamMember> getMembers() { return members; }
    public void setMembers(List<TeamMember> members) {
        this.members = members;
        members.forEach(m -> m.setTeam(this));
    }
}
