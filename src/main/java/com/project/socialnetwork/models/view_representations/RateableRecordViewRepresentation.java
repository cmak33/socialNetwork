package com.project.socialnetwork.models.view_representations;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class RateableRecordViewRepresentation {
    private Long userId;
    private Long id;
    private String text;
    private Date date;
    private Long likesCount;
    private Long dislikesCount;
    private List<String> imagesNames = new ArrayList<>();
    private boolean isLiked;
    private boolean isDisliked;
    private boolean isOwner;

    public boolean getLiked(){
        return isLiked;
    }

    public boolean getDisliked() {
        return isDisliked;
    }

    public boolean getOwner(){
        return isOwner;
    }
}
