package com.project.socialnetwork.services;

import com.project.socialnetwork.logic_classes.auxiliary_classes.AuxiliaryMethods;
import com.project.socialnetwork.models.entities.*;
import com.project.socialnetwork.repositories.RatingRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public record RatingService(RatingRepository ratingRepository,UserService userService) {

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


    public Optional<RecordRating> findByUserAndRecord(Long userId,Long recordId){
        return ratingRepository.findByUserIdAndRatedRecordId(userId,recordId);
    }

    public boolean isLiked(Long recordId){
        return hasCurrentUserRating(recordId,Like.class);
    }

    public boolean isDisliked(Long recordId){
        return hasCurrentUserRating(recordId, Dislike.class);
    }

    private boolean hasCurrentUserRating(Long recordId, Class<? extends RecordRating> ratingClass){
        return findByUserAndRecord(userService.receiveCurrentUserId(),recordId)
                .map(value->value.getClass().equals(ratingClass))
                .orElse(false);
    }


}
