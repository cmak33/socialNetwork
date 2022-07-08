package com.project.socialnetwork.services;

import com.project.socialnetwork.models.*;
import com.project.socialnetwork.repositories.RatingRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public record RatingService(RatingRepository ratingRepository) {

    public void addRating(RateableRecord ratedRecord,User user,RecordRating emptyRating){
        deleteUserRating(user.getId(),ratedRecord.getId());
        emptyRating.setRatedRecord(ratedRecord);
        emptyRating.setUser(user);
        ratingRepository.save(emptyRating);
    }

    public void deleteUserRating(Long userId,Long recordId){
        ratingRepository.deleteByUserIdAndRatedRecordId(userId,recordId);
    }

    public Optional<RecordRating> findRatingByUserAndRecord(Long userId,Long recordId){
        return ratingRepository.findByUserIdAndRatedRecordId(userId,recordId);
    }
}
