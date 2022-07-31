package com.sutej.broadcast.services;

import com.sutej.broadcast.dto.CommentsDto;
import com.sutej.broadcast.exception.BroadcastExceptiion;
import com.sutej.broadcast.exception.PostNotFoundException;
import com.sutej.broadcast.mapper.CommentsMapper;
import com.sutej.broadcast.modals.Comment;
import com.sutej.broadcast.modals.NotificationEmail;
import com.sutej.broadcast.modals.Post;
import com.sutej.broadcast.modals.User;
import com.sutej.broadcast.repository.CommentRepository;
import com.sutej.broadcast.repository.PostRepository;
import com.sutej.broadcast.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CommentsService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final CommentsMapper commentsMapper;
    private final CommentRepository commentRepository;
    private final MailService mailService;
    private final MailContentBuilder mailContentBuilder;

    public void save(CommentsDto commentsDto) throws BroadcastExceptiion {
        Post post = postRepository.findById(commentsDto.getPostId())
                .orElseThrow(()->new PostNotFoundException(commentsDto.getPostId().toString()));
        Comment comment = commentsMapper.MapDtoToComment(commentsDto);
        commentRepository.save(comment);

        String message = mailContentBuilder.build( post.getUser().getUserName() + " posted a comment on your post " + post.getUrl() );
        sendCommentNotification(message, post.getUser());
    }

    private void sendCommentNotification(String message, User user) throws BroadcastExceptiion {
        mailService.sendMail(new NotificationEmail(user.getUserName() + " Commented on your post ", user.getEmail(), message ));
    }

    public List<CommentsDto> getAllCommentsForPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow( ()-> new PostNotFoundException(postId.toString()));
        return commentRepository.findByPost(post)
                .stream()
                .map(commentsMapper::MapToDto)
                .collect(Collectors.toList());
    }

    public List<CommentsDto> getAllCommentsForUser(String userName) {
        User user = userRepository.findByuserName(userName).orElseThrow( ()-> new UsernameNotFoundException(userName));
        return commentRepository.findAllByuser(user)
                .stream()
                .map(commentsMapper::MapToDto)
                .collect(Collectors.toList());

    }
}
