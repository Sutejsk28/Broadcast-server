package com.sutej.broadcast.services;

import com.sutej.broadcast.dto.VoteDto;
import com.sutej.broadcast.exception.BroadcastExceptiion;
import com.sutej.broadcast.exception.PostNotFoundException;
import com.sutej.broadcast.modals.Post;
import com.sutej.broadcast.modals.Vote;
import com.sutej.broadcast.repository.PostRepository;
import com.sutej.broadcast.repository.VoteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.sutej.broadcast.modals.VoteType.UPVOTE;

@Service
@AllArgsConstructor
public class VoteService {

    private final VoteRepository voteRepository;
    private final PostRepository postRepository;
    private final AuthService authService;

    @Transactional
    public void vote(VoteDto voteDto) throws BroadcastExceptiion {
        Post post = postRepository.findById(voteDto.getPostId())
                .orElseThrow( ()->new PostNotFoundException(("Post not found with Id: " + voteDto.getPostId())));
        Optional<Vote> voteByPostAndUser = voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post, authService.getCurrentUser());
        if(voteByPostAndUser.isPresent() && voteByPostAndUser.get().getVoteType().equals(voteDto.getVoteType())){
            throw new BroadcastExceptiion("Already " + voteDto.getVoteType());
        }
        if(UPVOTE.equals(voteDto.getVoteType())){
            post.setVoteCount(post.getVoteCount() + 1);
        } else{
            post.setVoteCount(post.getVoteCount() - 1);
        }
        voteRepository.save(mapDtoToVote(voteDto, post));
    }

    private Vote mapDtoToVote(VoteDto voteDto, Post post) {
        return Vote.builder()
                .voteType(voteDto.getVoteType())
                .post(post)
                .user(authService.getCurrentUser())
                .build();
    }

}
