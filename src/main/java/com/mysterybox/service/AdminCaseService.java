package com.mysterybox.service;

import com.mysterybox.dto.AdminCaseDto;
import com.mysterybox.dto.AdminCaseRequest;
import com.mysterybox.dto.ClueDto;
import com.mysterybox.dto.EvidenceDto;
import com.mysterybox.entity.Clue;
import com.mysterybox.entity.Evidence;
import com.mysterybox.entity.MysteryCase;
import com.mysterybox.entity.Solution;
import com.mysterybox.exception.ResourceNotFoundException;
import com.mysterybox.repository.MysteryCaseRepository;
import com.mysterybox.repository.UserProgressRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminCaseService {

    private final MysteryCaseRepository caseRepository;
    private final UserProgressRepository progressRepository;

    public AdminCaseService(MysteryCaseRepository caseRepository,
                            UserProgressRepository progressRepository) {
        this.caseRepository = caseRepository;
        this.progressRepository = progressRepository;
    }

    @Transactional(readOnly = true)
    public List<AdminCaseDto> getAllAdminCases() {
        return caseRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(this::mapToAdminDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AdminCaseDto getAdminCaseById(Long id) {
        MysteryCase mysteryCase = caseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mystery case not found with ID: " + id));
        return mapToAdminDto(mysteryCase);
    }

    @Transactional
    public AdminCaseDto createCase(AdminCaseRequest request) {
        MysteryCase mysteryCase = new MysteryCase(
                request.getTitle().trim(),
                request.getDescription().trim(),
                request.getDifficulty(),
                request.isActive()
        );

        if (request.getClues() != null) {
            int order = 1;
            for (ClueDto cDto : request.getClues()) {
                if (cDto.getTitle() != null && !cDto.getTitle().trim().isEmpty()) {
                    Clue clue = new Clue(
                            mysteryCase,
                            cDto.getTitle().trim(),
                            cDto.getContent().trim(),
                            cDto.getDisplayOrder() != null ? cDto.getDisplayOrder() : order++
                    );
                    mysteryCase.addClue(clue);
                }
            }
        }

        if (request.getEvidence() != null) {
            for (EvidenceDto eDto : request.getEvidence()) {
                if (eDto.getTitle() != null && !eDto.getTitle().trim().isEmpty()) {
                    Evidence ev = new Evidence(
                            mysteryCase,
                            eDto.getTitle().trim(),
                            eDto.getDescription().trim()
                    );
                    mysteryCase.addEvidence(ev);
                }
            }
        }

        Solution solution = new Solution(mysteryCase, request.getCorrectAnswer().trim());
        mysteryCase.setSolution(solution);

        MysteryCase saved = caseRepository.save(mysteryCase);
        return mapToAdminDto(saved);
    }

    @Transactional
    public AdminCaseDto updateCase(Long id, AdminCaseRequest request) {
        MysteryCase mysteryCase = caseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mystery case not found with ID: " + id));

        mysteryCase.setTitle(request.getTitle().trim());
        mysteryCase.setDescription(request.getDescription().trim());
        mysteryCase.setDifficulty(request.getDifficulty());
        mysteryCase.setActive(request.isActive());

        // Update clues
        mysteryCase.getClues().clear();
        if (request.getClues() != null) {
            int order = 1;
            for (ClueDto cDto : request.getClues()) {
                if (cDto.getTitle() != null && !cDto.getTitle().trim().isEmpty()) {
                    Clue clue = new Clue(
                            mysteryCase,
                            cDto.getTitle().trim(),
                            cDto.getContent().trim(),
                            cDto.getDisplayOrder() != null ? cDto.getDisplayOrder() : order++
                    );
                    mysteryCase.addClue(clue);
                }
            }
        }

        // Update evidence
        mysteryCase.getEvidenceList().clear();
        if (request.getEvidence() != null) {
            for (EvidenceDto eDto : request.getEvidence()) {
                if (eDto.getTitle() != null && !eDto.getTitle().trim().isEmpty()) {
                    Evidence ev = new Evidence(
                            mysteryCase,
                            eDto.getTitle().trim(),
                            eDto.getDescription().trim()
                    );
                    mysteryCase.addEvidence(ev);
                }
            }
        }

        // Update solution
        if (mysteryCase.getSolution() != null) {
            mysteryCase.getSolution().setCorrectAnswer(request.getCorrectAnswer().trim());
        } else {
            Solution solution = new Solution(mysteryCase, request.getCorrectAnswer().trim());
            mysteryCase.setSolution(solution);
        }

        MysteryCase updated = caseRepository.save(mysteryCase);
        return mapToAdminDto(updated);
    }

    @Transactional
    public void deleteCase(Long id) {
        MysteryCase mysteryCase = caseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mystery case not found with ID: " + id));

        // Delete any related progress records first to maintain referential integrity
        progressRepository.findAll().stream()
                .filter(p -> p.getMysteryCase().getId().equals(id))
                .forEach(progressRepository::delete);

        caseRepository.delete(mysteryCase);
    }

    private AdminCaseDto mapToAdminDto(MysteryCase c) {
        AdminCaseDto dto = new AdminCaseDto();
        dto.setId(c.getId());
        dto.setTitle(c.getTitle());
        dto.setDescription(c.getDescription());
        dto.setDifficulty(c.getDifficulty());
        dto.setActive(c.isActive());
        dto.setCreatedAt(c.getCreatedAt());

        if (c.getSolution() != null) {
            dto.setCorrectAnswer(c.getSolution().getCorrectAnswer());
        }

        dto.setClues(c.getClues().stream()
                .map(clue -> new ClueDto(clue.getId(), clue.getTitle(), clue.getContent(), clue.getDisplayOrder()))
                .collect(Collectors.toList()));

        dto.setEvidence(c.getEvidenceList().stream()
                .map(ev -> new EvidenceDto(ev.getId(), ev.getTitle(), ev.getDescription()))
                .collect(Collectors.toList()));

        return dto;
    }
}
