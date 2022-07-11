package com.project.socialnetwork.services;

import com.project.socialnetwork.logic_classes.auxiliary_classes.AuxiliaryMethods;
import com.project.socialnetwork.models.entities.RateableRecord;
import com.project.socialnetwork.models.entities.RecordRating;
import com.project.socialnetwork.models.entities.User;
import com.project.socialnetwork.repositories.RatingRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public record RatingService(RatingRepository ratingRepository) {

    public void addRating(RateableRecord ratedRecord, User user, RecordRating emptyRating){
        Optional<RecordRating> oldRating = findByUserAndRecord(user.getId(),ratedRecord.getId());
        boolean shouldAddNewRating = true;
        if(oldRating.isPresent()){
            ratingRepository.delete(oldRating.get());
            shouldAddNewRating = !AuxiliaryMethods.areSameTypes(oldRating.get(),emptyRating);
        }
        if(shouldAddNewRating) {
            emptyRating.setRatedRecord(ratedRecord);
            emptyRating.setUser(user);
            ratingRepository.save(emptyRating);
        }
    }


    public void deleteUserRating(Long userId,Long recordId){
        ratingRepository.deleteByUserIdAndRatedRecordId(userId,recordId);
    }

    public Optional<RecordRating> findByUserAndRecord(Long userId,Long recordId){
        return ratingRepository.findByUserIdAndRatedRecordId(userId,recordId);
    }
}
