package com.sutej.broadcast.repository;

import com.sutej.broadcast.modals.Post;
import com.sutej.broadcast.modals.User;
import com.sutej.broadcast.modals.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteRepository  extends JpaRepository<Vote, Long> {
    Optional<Vote> findTopByPostAndUserOrderByVoteIdDesc(Post post, User user);
}
