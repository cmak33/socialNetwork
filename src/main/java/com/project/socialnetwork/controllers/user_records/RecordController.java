package com.project.socialnetwork.controllers.user_records;

import com.project.socialnetwork.logic_classes.auxiliary_classes.AuxiliaryMethods;
import com.project.socialnetwork.models.dtos.RateableRecordDTO;
import com.project.socialnetwork.models.entities.*;
import com.project.socialnetwork.models.view_representations.RateableRecordViewRepresentation;
import com.project.socialnetwork.services.RecordService;
import com.project.socialnetwork.services.entity_to_view_representation_converters.RecordToViewRepresentationConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/records")
@RequiredArgsConstructor
public class RecordController {
    private final RecordService recordService;
    private final RecordToViewRepresentationConverter converter;


    @GetMapping("/postedRecord/{id}")
    public String showPostedRecordById(@PathVariable Long id, Model model){
        Optional<PostedRecord> postedRecord = recordService.findByIdAndType(id,PostedRecord.class);
        return postedRecord.map(value->{
            List<RateableRecordViewRepresentation> commentsViewRepresentation = value.getComments().stream().map(converter::convertToViewRepresentation).toList();
            RateableRecordViewRepresentation postedRecordRepresentation = converter.convertToViewRepresentation(value);
            model.addAttribute("postedRecord",postedRecordRepresentation);
            model.addAttribute("comments",commentsViewRepresentation);
            model.addAttribute("newComment",new Comment());
            return "record/posted_record";
        }).orElse("pageNotFound/pageNotFoundView");
    }

    @GetMapping("/create")
    public String createRecord(Model model){
        RateableRecordDTO dto = new RateableRecordDTO();
        dto.setEntityClass(PostedRecord.class.getName());
        model.addAttribute("record",dto);
        return "record/create";
    }

    @PostMapping("/create")
    public String createRecordPost(@ModelAttribute("record") @Valid RateableRecordDTO record, BindingResult result, @ModelAttribute("pictures") MultipartFile[] pictures){
        if(!result.hasErrors()) {
            recordService.saveNewRecordByDTO(record,pictures);
        }
        return String.format("redirect:/profiles/%d/records", recordService.userService().receiveCurrentUserId());
    }

    @PostMapping("/{id}/create_comment")
    public String createComment(@PathVariable Long id,@ModelAttribute("newComment") @Valid Comment comment,BindingResult result,@ModelAttribute("pictures") MultipartFile[] pictures){
        if(!result.hasErrors()) {
            recordService.saveComment(comment, id);
            if (AuxiliaryMethods.isMultipartFilesArrayNotEmpty(pictures)) {
                comment.setImagesNames(recordService.savePictures(comment.getId(), Arrays.stream(pictures).toList()));
                recordService.saveRecord(comment);
            }
        }
        return String.format("redirect:/records/postedRecord/%d",id);
    }

    @GetMapping("/{id}/edit")
    public String editRecord(@PathVariable Long id,Model model){
        Optional<RateableRecordDTO> record = recordService.findDtoByIdAndType(id,RateableRecord.class);
        if(record.isPresent() && recordService.isCurrentUserOwner(record.get().getId())){
            model.addAttribute("record",record.get());
            return "record/edit";
        } else{
            return "pageNotFound/pageNotFoundView";
        }
    }

    @PostMapping("/{id}/edit")
    public String editRecordPost(@ModelAttribute("record") @Valid RateableRecordDTO dto,BindingResult result,@ModelAttribute("pictures") MultipartFile[] pictures){
        if(!result.hasErrors()){
            if(AuxiliaryMethods.isMultipartFilesArrayNotEmpty(pictures)){
                List<String> newImagesNames = recordService.savePictures(dto.getId(), Arrays.stream(pictures).toList());
                dto.getImagesNames().addAll(newImagesNames);
            }
            recordService.saveRecordByDto(dto);
        }
        return "record/edit";
    }


    @PostMapping("/{id}/deletePicture/{index}")
    public String deletePicture(@PathVariable("id") Long id,@PathVariable("index") int index){
        recordService.deleteImageByIndex(id,index);
        return String.format("redirect:/records/%d/edit",id);
    }

    @PostMapping("/{id}/delete")
    public String deleteRecord(@PathVariable Long id){
        recordService.deleteById(id);
        return "redirect:/profiles/my_profile";
    }

}
