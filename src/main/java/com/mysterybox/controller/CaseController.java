package com.mysterybox.controller;

import com.mysterybox.dto.*;
import com.mysterybox.service.CaseService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cases")
public class CaseController {

    private final CaseService caseService;

    public CaseController(CaseService caseService) {
        this.caseService = caseService;
    }

    @GetMapping
    public ResponseEntity<List<CaseSummaryDto>> getAllCases(Authentication authentication) {
        String email = authentication != null ? authentication.getName() : null;
        return ResponseEntity.ok(caseService.getAllCases(email));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CaseDetailDto> getCaseById(@PathVariable Long id, Authentication authentication) {
        String email = authentication != null ? authentication.getName() : null;
        return ResponseEntity.ok(caseService.getCaseById(id, email));
    }

    @GetMapping("/{id}/clues")
    public ResponseEntity<List<ClueDto>> getCaseClues(@PathVariable Long id) {
        return ResponseEntity.ok(caseService.getCaseClues(id));
    }

    @GetMapping("/{id}/evidence")
    public ResponseEntity<List<EvidenceDto>> getCaseEvidence(@PathVariable Long id) {
        return ResponseEntity.ok(caseService.getCaseEvidence(id));
    }

    @PostMapping("/{id}/submit")
    public ResponseEntity<SubmitAnswerResponse> submitAnswer(@PathVariable Long id,
                                                            @Valid @RequestBody SubmitAnswerRequest request,
                                                            Authentication authentication) {
        String email = authentication.getName();
        SubmitAnswerResponse response = caseService.submitAnswer(id, request.getAnswer(), email);
        return ResponseEntity.ok(response);
    }
}
