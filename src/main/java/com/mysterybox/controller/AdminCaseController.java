package com.mysterybox.controller;

import com.mysterybox.dto.AdminCaseDto;
import com.mysterybox.dto.AdminCaseRequest;
import com.mysterybox.dto.ApiResponse;
import com.mysterybox.service.AdminCaseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/cases")
@PreAuthorize("hasRole('ADMIN')")
public class AdminCaseController {

    private final AdminCaseService adminCaseService;

    public AdminCaseController(AdminCaseService adminCaseService) {
        this.adminCaseService = adminCaseService;
    }

    @GetMapping
    public ResponseEntity<List<AdminCaseDto>> getAllAdminCases() {
        return ResponseEntity.ok(adminCaseService.getAllAdminCases());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdminCaseDto> getAdminCaseById(@PathVariable Long id) {
        return ResponseEntity.ok(adminCaseService.getAdminCaseById(id));
    }

    @PostMapping
    public ResponseEntity<AdminCaseDto> createCase(@Valid @RequestBody AdminCaseRequest request) {
        AdminCaseDto created = adminCaseService.createCase(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdminCaseDto> updateCase(@PathVariable Long id,
                                                   @Valid @RequestBody AdminCaseRequest request) {
        AdminCaseDto updated = adminCaseService.updateCase(id, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteCase(@PathVariable Long id) {
        adminCaseService.deleteCase(id);
        return ResponseEntity.ok(new ApiResponse(true, "Case deleted successfully"));
    }
}
