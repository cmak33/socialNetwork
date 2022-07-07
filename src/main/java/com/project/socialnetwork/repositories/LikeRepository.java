package com.project.socialnetwork.repositories;

import com.project.socialnetwork.models.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface LikeRepository extends JpaRepository<Like,Long>{
    Optional<Like> findByUserIdAndRatedRecordId(Long userId, Long ratedRecordId);
}
