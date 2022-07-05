package com.project.socialnetwork.services;

import com.project.socialnetwork.models.Post;
import com.project.socialnetwork.repositories.RecordRepository;
import com.project.socialnetwork.services.picture_save_services.RecordPictureService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public record RecordService(RecordRepository recordRepository, RecordPictureService recordPictureOperations) {


    public void savePost(Post post) {
        post.convertImagesNamesListToJson();
        recordRepository.save(post);
    }

    public void deletePost(Post post){
        recordRepository.delete(post);
        recordPictureOperations.deletePostPictures(post);
    }

    public Optional<Post> findById(Long id) {
        Optional<Post> post = recordRepository.findById(id);
        post.ifPresent(Post::convertJsonToImagesNamesList);
        return post;
    }

    public void deleteImage(String imageName, Post post) {
        post.getImagesNames().remove(imageName);
        recordPictureOperations.deletePicture(imageName);
    }

    public void addPictures(Post post, List<MultipartFile> pictures){
        List<String> pictureNames = recordPictureOperations.savePostPictures(post,pictures);
        post.setImagesNames(pictureNames);
    }

}
