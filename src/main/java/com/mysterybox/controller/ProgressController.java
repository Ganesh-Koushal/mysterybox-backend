package com.mysterybox.controller;

import com.mysterybox.dto.UserProgressDto;
import com.mysterybox.service.ProgressService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/progress")
public class ProgressController {

    private final ProgressService progressService;

    public ProgressController(ProgressService progressService) {
        this.progressService = progressService;
    }

    @GetMapping
    public ResponseEntity<UserProgressDto> getUserProgress(Authentication authentication) {
        String email = authentication.getName();
        UserProgressDto progress = progressService.getUserProgress(email);
        return ResponseEntity.ok(progress);
    }
}
