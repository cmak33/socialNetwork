package com.project.socialnetwork.repositories;

import com.project.socialnetwork.models.Dislike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DislikeRepository extends JpaRepository<Dislike,Long> {
    Optional<Dislike> findByUserIdAndRatedRecordId(Long userId, Long ratedRecordId);
}
