package com.project.socialnetwork.services;

import com.project.socialnetwork.models.Post;
import com.project.socialnetwork.repositories.PostRepository;
import com.project.socialnetwork.services.picture_save_services.PostPictureService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public record PostService(PostRepository postRepository, PostPictureService postPictureOperations) {


    public void savePost(Post post) {
        post.convertImagesNamesListToJson();
        postRepository.save(post);
    }

    public void deletePost(Post post){
        postRepository.delete(post);
        postPictureOperations.deletePostPictures(post);
    }

    public Optional<Post> findById(Long id) {
        Optional<Post> post = postRepository.findById(id);
        post.ifPresent(Post::convertJsonToImagesNamesList);
        return post;
    }

    public void deleteImage(String imageName, Post post) {
        post.getImagesNames().remove(imageName);
        postPictureOperations.deletePicture(imageName);
    }

    public void addPictures(Post post, List<MultipartFile> pictures){
        List<String> pictureNames = postPictureOperations.savePostPictures(post,pictures);
        post.setImagesNames(pictureNames);
    }

}
