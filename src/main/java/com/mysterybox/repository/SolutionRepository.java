package com.mysterybox.repository;

import com.mysterybox.entity.Solution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SolutionRepository extends JpaRepository<Solution, Long> {
    Optional<Solution> findByMysteryCaseId(Long caseId);
}
