package com.project.socialnetwork.repositories;

import com.project.socialnetwork.models.RecordRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingRepository extends JpaRepository<RecordRating,Long> {
}
