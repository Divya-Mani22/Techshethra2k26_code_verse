package com.example.hackthon.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import com.example.hackthon.entity.Registration;
import com.example.hackthon.repository.RegistrationRepository;

import java.time.LocalDateTime; // Date check-kaga add panniyathu
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/register")
@CrossOrigin(origins = "http://localhost:4200")
public class RegistrationController {

    private final RegistrationRepository repo;

    @Autowired
    private JavaMailSender mailSender;

    public RegistrationController(RegistrationRepository repo) {
        this.repo = repo;
    }

    // 1. CREATE: With Deadline and Duplicate Check
    @PostMapping
    public ResponseEntity<?> save(@RequestBody Registration r) {
        // --- REGISTRATION CLOSE LOGIC ---
        // Unga deadline-a inga fix pannunga (Year, Month, Day, Hour, Minute)
        LocalDateTime deadline = LocalDateTime.of(2026, 3, 01, 23, 59); 
        LocalDateTime now = LocalDateTime.now();

        if (now.isAfter(deadline)) {
            return ResponseEntity.badRequest().body("Error: Registration has been closed officially!");
        }
        // --------------------------------

        if (r.getTransactionId() == null || r.getTransactionId().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Error: Transaction ID is required!");
        }

        if (repo.existsByTransactionId(r.getTransactionId())) {
            return ResponseEntity.badRequest().body("Error: This Transaction ID has already been used!");
        }

        r.setPaymentStatus("UNDER_REVIEW");
        Registration saved = repo.save(r);

        try {
            sendRegistrationEmail(saved);
        } catch (Exception e) {
            System.err.println("Initial Email Failed: " + e.getMessage());
        }

        return ResponseEntity.ok(saved);
    }

    // 2. READ: Admin dashboard view for 200 members
    @GetMapping("/all")
    public List<Registration> getAll() {
        return repo.findAll();
    }

    // 3. UPDATE: Status update with automated Email
    @PutMapping("/update-status/{id}")
    public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> statusUpdate) {
        return repo.findById(id).map(registration -> {
            String newStatus = statusUpdate.get("status");
            registration.setPaymentStatus(newStatus);
            repo.save(registration);

            if ("PAID".equalsIgnoreCase(newStatus)) {
                try {
                    sendPaymentSuccessEmail(registration);
                } catch (Exception e) {
                    System.err.println("Payment Success Email Failed: " + e.getMessage());
                }
            } 
            else if ("REJECTED".equalsIgnoreCase(newStatus)) {
                try {
                    sendRejectionEmail(registration);
                } catch (Exception e) {
                    System.err.println("Rejection Email Failed: " + e.getMessage());
                }
            }
            return ResponseEntity.ok().build();
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/count")
    public long getCount() {
        return repo.count();
    }

    // --- Helper Methods ---

    private void sendRegistrationEmail(Registration r) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(r.getLeaderEmail());
        message.setSubject("Registration Received: Team " + r.getTeamName());
        message.setText("Dear " + r.getLeaderName() + ",\n\n" +
                "Thank you for registering. Your details for Team '" + r.getTeamName() + "' are under review.\n" +
                "Transaction ID: " + r.getTransactionId() + "\n\n" +
                "Best Regards,\nHackathon Team");
        mailSender.send(message);
    }

    private void sendPaymentSuccessEmail(Registration r) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(r.getLeaderEmail());
        message.setSubject("Payment Verified: Welcome to Code-verse!");
        
        String whatsappLink = "https://chat.whatsapp.com/LU8xM1qf4wRAupTnx7dB9J";

        message.setText("Dear " + r.getLeaderName() + ",\n\n" +
                "Congratulations! Your payment for Team '" + r.getTeamName() + "' has been verified.\n\n" +
                "Join official WhatsApp group: " + whatsappLink + "\n\n" +
                "Best Regards,\nHackathon Organizing Team");
        mailSender.send(message);
    }

    private void sendRejectionEmail(Registration r) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(r.getLeaderEmail());
        message.setSubject("Registration Update: REJECTED");
        message.setText("Dear " + r.getLeaderName() + ",\n\n" +
                "Your registration for Team '" + r.getTeamName() + "' was REJECTED due to invalid Transaction ID (" + r.getTransactionId() + ").\n\n" +
                "Best Regards,\nHackathon Team");
        mailSender.send(message);
    }
}
