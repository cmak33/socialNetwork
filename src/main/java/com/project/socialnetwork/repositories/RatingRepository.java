package com.project.socialnetwork.repositories;

import com.project.socialnetwork.models.entities.RecordRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<RecordRating,Long> {
    Optional<RecordRating> findByUserIdAndRatedRecordId(Long userId, Long ratedRecordId);

    void deleteByUserIdAndRatedRecordId(Long userId,Long ratedRecordId);
}
