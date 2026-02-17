package com.example.hackthon.RegistrationService;


import org.springframework.stereotype.Service;

import com.example.hackthon.entity.Registration;
import com.example.hackthon.repository.RegistrationRepository;

@Service
public class RegistrationService {

    private final RegistrationRepository repository;

    // Constructor Injection (BEST PRACTICE)
    public RegistrationService(RegistrationRepository repository) {
        this.repository = repository;
    }

    // Save registration
    public Registration save(Registration registration) {
        return repository.save(registration);
    }
}

