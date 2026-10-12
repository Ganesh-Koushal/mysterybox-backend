package com.mysterybox.repository;

import com.mysterybox.entity.UserProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserProgressRepository extends JpaRepository<UserProgress, Long> {
    Optional<UserProgress> findByUserIdAndMysteryCaseId(Long userId, Long caseId);

    List<UserProgress> findByUserId(Long userId);

    @Query("SELECT COALESCE(SUM(up.scoreAwarded), 0) FROM UserProgress up WHERE up.user.id = :userId AND up.isSolved = true")
    Integer getTotalScoreByUserId(@Param("userId") Long userId);

    long countByUserIdAndIsSolvedTrue(Long userId);
}
