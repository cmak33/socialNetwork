package com.project.socialnetwork.repositories;

import com.project.socialnetwork.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post,Long> {
}
