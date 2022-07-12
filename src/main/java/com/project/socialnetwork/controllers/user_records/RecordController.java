package com.project.socialnetwork.controllers.user_records;

import com.project.socialnetwork.models.entities.Dislike;
import com.project.socialnetwork.models.entities.Like;
import com.project.socialnetwork.models.entities.PostedRecord;
import com.project.socialnetwork.models.entities.RecordRating;
import com.project.socialnetwork.repositories.RatingRepository;
import com.project.socialnetwork.services.RecordService;
import com.project.socialnetwork.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.Optional;

@Controller
@RequestMapping("/records")
@RequiredArgsConstructor
@SessionAttributes("record")
public class RecordController {
    private final RecordService recordService;
    private final UserService userService;
    private final RatingRepository ratingRepository;

    @ModelAttribute("record")
    public PostedRecord currentRecord(){
        return new PostedRecord();
    }

    @GetMapping("/{id}")
    public String showRecordById(@PathVariable Long id, Model model){
        Optional<PostedRecord> postedRecord = recordService.findById(id);
        return postedRecord.map(value->{
            model.addAttribute("record",value);
            boolean isOwner = recordService.isOwner(value, userService.receiveCurrentUser());
            model.addAttribute("isOwner",isOwner);
            boolean isLiked = false;
            boolean isDisliked = false;
            Optional<RecordRating> rating = ratingRepository.findByUserIdAndRatedRecordId(userService.receiveCurrentUser().getId(), value.getId());
            if(rating.isPresent()){
                isLiked = Like.class.equals(rating.get().getClass());
                isDisliked = Dislike.class.equals(rating.get().getClass());
            }
            model.addAttribute("isLiked",isLiked);
            model.addAttribute("isDisliked",isDisliked);
            return "record/record";
        }).orElse("pageNotFound/pageNotFoundView");
    }

    @GetMapping("/create")
    public String createRecord(Model model){
        model.addAttribute("record",new PostedRecord());
        return "record/create";
    }

    @PostMapping("/create")
    public String createRecordPost(@ModelAttribute("record") @Valid PostedRecord postedRecord,BindingResult result,@ModelAttribute("pictures") MultipartFile[] pictures){
        if(!result.hasErrors()) {
            postedRecord.setUser(userService.receiveCurrentUser());
            recordService.saveRecord(postedRecord);
            if(isMultipartFilesArrayNotEmpty(pictures)) {
                recordService.addPictures(postedRecord, Arrays.stream(pictures).toList());
                recordService.saveRecord(postedRecord);
            }
        }
        return "record/create";
    }

    @GetMapping("/{id}/edit")
    public String editRecord(@PathVariable Long id,Model model){
        Optional<PostedRecord> record = recordService.findById(id);
        if(record.isPresent() && userService.isCurrentUserOwnerOfRecord(record.get())){
            model.addAttribute("record",record.get());
            return "record/edit";
        } else{
            return "pageNotFound/pageNotFoundView";
        }
    }

    @PostMapping("/{id}/edit")
    public String editRecordPost(@ModelAttribute("record") @Valid PostedRecord postedRecord,BindingResult result,@ModelAttribute("pictures") MultipartFile[] pictures){
        if(!result.hasErrors()){
            if(isMultipartFilesArrayNotEmpty(pictures)){
                recordService.addPictures(postedRecord, Arrays.stream(pictures).toList());
            }
            recordService.saveRecord(postedRecord);
        }
        return "record/edit";
    }

    private boolean isMultipartFilesArrayNotEmpty(MultipartFile[] multipartFiles){
        return multipartFiles.length > 0 && !multipartFiles[0].isEmpty();
    }

    @PostMapping("/{id}/deletePicture/{index}")
    public String deletePicture(@PathVariable("id") Long id,@PathVariable("index") int index){
        Optional<PostedRecord> record = recordService.findById(id);
        record.ifPresent(value->{
            recordService.deleteImage(value.getImagesNames().get(index),value);
            recordService.saveRecord(value);
        });
        return String.format("redirect:/records/%d/edit",id);
    }

    @PostMapping("/{id}/delete")
    public String deleteRecord(@PathVariable Long id){
        Optional<PostedRecord> record = recordService.findById(id);
        record.ifPresent(recordService::deleteRecord);
        return "redirect:/profiles/my_profile";
    }

    @PostMapping("/{id}/like")
    public String like(@PathVariable Long id){
        return String.format("redirect:/records/%d",id);
    }
}
