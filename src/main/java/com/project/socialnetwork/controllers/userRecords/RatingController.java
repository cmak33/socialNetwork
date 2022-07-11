package com.project.socialnetwork.controllers.userRecords;

import com.project.socialnetwork.logic_classes.auxiliary_classes.AuxiliaryMethods;
import com.project.socialnetwork.models.*;
import com.project.socialnetwork.repositories.RateableRecordRepository;
import com.project.socialnetwork.services.RatingService;
import com.project.socialnetwork.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/rating")
public class RatingController {
    private final RatingService ratingService;
    private final UserService userService;
    private final RateableRecordRepository rateableRecordRepository;

    @PostMapping("/{id}/like")
    public String like(@PathVariable Long id,HttpServletRequest servletRequest){
        addRating(id,new Like());
        return AuxiliaryMethods.createRedirectionToPreviousPage(servletRequest);
    }

    @PostMapping("/{id}/dislike")
    public String dislike(@PathVariable Long id,HttpServletRequest servletRequest){
        addRating(id,new Dislike());
        return AuxiliaryMethods.createRedirectionToPreviousPage(servletRequest);
    }

    private void addRating(Long recordId, RecordRating rating){
        Optional<RateableRecord> record = rateableRecordRepository.findById(recordId);
        record.ifPresent(value->{
            User user = userService.receiveCurrentUser();
            ratingService.addRating(value,user,rating);
        });
    }


}
