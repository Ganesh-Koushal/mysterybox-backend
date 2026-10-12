package com.mysterybox.service;

import com.mysterybox.dto.*;
import com.mysterybox.entity.*;
import com.mysterybox.exception.BadRequestException;
import com.mysterybox.exception.ResourceNotFoundException;
import com.mysterybox.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CaseService {

    private final MysteryCaseRepository caseRepository;
    private final ClueRepository clueRepository;
    private final EvidenceRepository evidenceRepository;
    private final SolutionRepository solutionRepository;
    private final UserProgressRepository progressRepository;
    private final UserRepository userRepository;

    public CaseService(MysteryCaseRepository caseRepository,
                       ClueRepository clueRepository,
                       EvidenceRepository evidenceRepository,
                       SolutionRepository solutionRepository,
                       UserProgressRepository progressRepository,
                       UserRepository userRepository) {
        this.caseRepository = caseRepository;
        this.clueRepository = clueRepository;
        this.evidenceRepository = evidenceRepository;
        this.solutionRepository = solutionRepository;
        this.progressRepository = progressRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<CaseSummaryDto> getAllCases(String currentUserEmail) {
        List<MysteryCase> cases = caseRepository.findByIsActiveTrueOrderByCreatedAtDesc();
        Set<Long> solvedCaseIds = getSolvedCaseIds(currentUserEmail);

        return cases.stream().map(c -> new CaseSummaryDto(
                c.getId(),
                c.getTitle(),
                c.getDescription(),
                c.getDifficulty(),
                c.isActive(),
                solvedCaseIds.contains(c.getId()),
                c.getCreatedAt()
        )).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CaseDetailDto getCaseById(Long id, String currentUserEmail) {
        MysteryCase mysteryCase = caseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mystery case not found with ID: " + id));

        if (!mysteryCase.isActive()) {
            throw new ResourceNotFoundException("Mystery case is currently inactive");
        }

        Set<Long> solvedCaseIds = getSolvedCaseIds(currentUserEmail);

        CaseDetailDto dto = new CaseDetailDto();
        dto.setId(mysteryCase.getId());
        dto.setTitle(mysteryCase.getTitle());
        dto.setDescription(mysteryCase.getDescription());
        dto.setDifficulty(mysteryCase.getDifficulty());
        dto.setActive(mysteryCase.isActive());
        dto.setSolved(solvedCaseIds.contains(mysteryCase.getId()));
        dto.setCreatedAt(mysteryCase.getCreatedAt());

        dto.setClues(mysteryCase.getClues().stream()
                .map(clue -> new ClueDto(clue.getId(), clue.getTitle(), clue.getContent(), clue.getDisplayOrder()))
                .collect(Collectors.toList()));

        dto.setEvidence(mysteryCase.getEvidenceList().stream()
                .map(ev -> new EvidenceDto(ev.getId(), ev.getTitle(), ev.getDescription()))
                .collect(Collectors.toList()));

        return dto;
    }

    @Transactional(readOnly = true)
    public List<ClueDto> getCaseClues(Long caseId) {
        if (!caseRepository.existsById(caseId)) {
            throw new ResourceNotFoundException("Mystery case not found with ID: " + caseId);
        }
        return clueRepository.findByMysteryCaseIdOrderByDisplayOrderAsc(caseId).stream()
                .map(clue -> new ClueDto(clue.getId(), clue.getTitle(), clue.getContent(), clue.getDisplayOrder()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EvidenceDto> getCaseEvidence(Long caseId) {
        if (!caseRepository.existsById(caseId)) {
            throw new ResourceNotFoundException("Mystery case not found with ID: " + caseId);
        }
        return evidenceRepository.findByMysteryCaseId(caseId).stream()
                .map(ev -> new EvidenceDto(ev.getId(), ev.getTitle(), ev.getDescription()))
                .collect(Collectors.toList());
    }

    @Transactional
    public SubmitAnswerResponse submitAnswer(Long caseId, String proposedAnswer, String userEmail) {
        if (proposedAnswer == null || proposedAnswer.trim().isEmpty()) {
            throw new BadRequestException("Submitted solution cannot be blank");
        }

        User user = userRepository.findByEmail(userEmail.trim().toLowerCase())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        MysteryCase mysteryCase = caseRepository.findById(caseId)
                .orElseThrow(() -> new ResourceNotFoundException("Mystery case not found with ID: " + caseId));

        if (!mysteryCase.isActive()) {
            throw new BadRequestException("Cannot submit solutions for an inactive case");
        }

        Solution solution = solutionRepository.findByMysteryCaseId(caseId)
                .orElseThrow(() -> new BadRequestException("Case has no configured solution to evaluate against"));

        int currentTotalScore = progressRepository.getTotalScoreByUserId(user.getId());
        Optional<UserProgress> existingProgressOpt = progressRepository.findByUserIdAndMysteryCaseId(user.getId(), caseId);

        // Check if already solved
        if (existingProgressOpt.isPresent() && existingProgressOpt.get().isSolved()) {
            return new SubmitAnswerResponse(
                    true,
                    true,
                    0,
                    currentTotalScore,
                    "Case is already solved! You have already earned the points for this case."
            );
        }

        boolean isCorrect = evaluateAnswer(proposedAnswer, solution.getCorrectAnswer());

        if (isCorrect) {
            int pointsAwarded = 100;
            UserProgress progress = existingProgressOpt.orElseGet(() -> new UserProgress(user, mysteryCase, false, 0, null));
            progress.setSolved(true);
            progress.setScoreAwarded(pointsAwarded);
            progress.setSolvedAt(LocalDateTime.now());
            progressRepository.save(progress);

            int updatedTotal = currentTotalScore + pointsAwarded;
            return new SubmitAnswerResponse(
                    true,
                    false,
                    pointsAwarded,
                    updatedTotal,
                    "Case Solved! Outstanding deduction, Detective. +100 Points awarded!"
            );
        } else {
            return new SubmitAnswerResponse(
                    false,
                    false,
                    0,
                    currentTotalScore,
                    "Incorrect deduction. Re-examine the clues, inspect the evidence, and try again."
            );
        }
    }

    private boolean evaluateAnswer(String userInput, String correctAnswer) {
        if (userInput == null || correctAnswer == null) {
            return false;
        }

        String userNorm = normalizeString(userInput);
        String correctNorm = normalizeString(correctAnswer);

        if (userNorm.equals(correctNorm)) {
            return true;
        }

        // Substring check for names or key phrases if length is significant
        if (userNorm.length() >= 4 && correctNorm.contains(userNorm)) {
            return true;
        }
        if (correctNorm.length() >= 4 && userNorm.contains(correctNorm)) {
            return true;
        }

        return false;
    }

    private String normalizeString(String input) {
        return input.toLowerCase()
                .replaceAll("[^a-z0-9]", " ")
                .replaceAll("\\s+", " ")
                .trim();
    }

    private Set<Long> getSolvedCaseIds(String userEmail) {
        if (userEmail == null || userEmail.trim().isEmpty()) {
            return Collections.emptySet();
        }
        return userRepository.findByEmail(userEmail.trim().toLowerCase())
                .map(u -> progressRepository.findByUserId(u.getId()).stream()
                        .filter(UserProgress::isSolved)
                        .map(p -> p.getMysteryCase().getId())
                        .collect(Collectors.toSet()))
                .orElse(Collections.emptySet());
    }
}
