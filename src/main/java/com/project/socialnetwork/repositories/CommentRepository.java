package com.project.socialnetwork.repositories;

import com.project.socialnetwork.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment,Long> {
}
