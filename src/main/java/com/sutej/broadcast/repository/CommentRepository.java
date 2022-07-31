package com.sutej.broadcast.repository;

import com.sutej.broadcast.modals.Comment;
import com.sutej.broadcast.modals.Post;
import com.sutej.broadcast.modals.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository  extends JpaRepository<Comment, Long> {

    List<Comment> findByPost(Post post);

    List<Comment> findAllByuser(User user);

    Integer countByPost(Post post);
}
