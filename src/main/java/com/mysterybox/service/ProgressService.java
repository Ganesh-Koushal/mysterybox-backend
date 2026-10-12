package com.mysterybox.service;

import com.mysterybox.dto.SolvedCaseSummaryDto;
import com.mysterybox.dto.UserProgressDto;
import com.mysterybox.entity.User;
import com.mysterybox.entity.UserProgress;
import com.mysterybox.exception.ResourceNotFoundException;
import com.mysterybox.repository.MysteryCaseRepository;
import com.mysterybox.repository.UserProgressRepository;
import com.mysterybox.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProgressService {

    private final UserProgressRepository progressRepository;
    private final UserRepository userRepository;
    private final MysteryCaseRepository caseRepository;

    public ProgressService(UserProgressRepository progressRepository,
                           UserRepository userRepository,
                           MysteryCaseRepository caseRepository) {
        this.progressRepository = progressRepository;
        this.userRepository = userRepository;
        this.caseRepository = caseRepository;
    }

    @Transactional(readOnly = true)
    public UserProgressDto getUserProgress(String userEmail) {
        User user = userRepository.findByEmail(userEmail.trim().toLowerCase())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + userEmail));

        Integer totalScore = progressRepository.getTotalScoreByUserId(user.getId());
        if (totalScore == null) {
            totalScore = 0;
        }

        long solvedCount = progressRepository.countByUserIdAndIsSolvedTrue(user.getId());
        long totalActiveCases = caseRepository.count();

        List<UserProgress> userProgressList = progressRepository.findByUserId(user.getId());
        List<SolvedCaseSummaryDto> solvedCases = userProgressList.stream()
                .filter(UserProgress::isSolved)
                .map(up -> new SolvedCaseSummaryDto(
                        up.getMysteryCase().getId(),
                        up.getMysteryCase().getTitle(),
                        up.getMysteryCase().getDifficulty(),
                        up.getScoreAwarded(),
                        up.getSolvedAt()
                ))
                .collect(Collectors.toList());

        String rank = determineRank(totalScore);

        return new UserProgressDto(totalScore, solvedCount, totalActiveCases, rank, solvedCases);
    }

    private String determineRank(int score) {
        if (score >= 400) return "Consulting Detective (Master)";
        if (score >= 300) return "Chief Inspector";
        if (score >= 200) return "Senior Investigator";
        if (score >= 100) return "Field Detective";
        return "Rookie Sleuth";
    }
}
