package com.sutej.broadcast.mapper;

import com.sutej.broadcast.dto.CommentsDto;
import com.sutej.broadcast.modals.Comment;
import com.sutej.broadcast.repository.PostRepository;
import com.sutej.broadcast.services.AuthService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class CommentsMapper {

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private AuthService authService;
    @Autowired
    private PostRepository postRepository;

    public CommentsDto MapToDto(Comment comment){
        this.modelMapper = new ModelMapper();
        TypeMap<Comment, CommentsDto> propertyMapper = this.modelMapper.createTypeMap(Comment.class, CommentsDto.class);

        propertyMapper.addMappings(
                mapper -> mapper.map(src -> src.getPost().getPostId(), CommentsDto::setPostId)
        );
        propertyMapper.addMappings(
                mapper -> mapper.map(src -> comment.getUser().getUserName(), CommentsDto::setUserName)
        );

        CommentsDto commentsDto = modelMapper.map(comment, CommentsDto.class);
        return commentsDto;

    }

    public Comment MapDtoToComment(CommentsDto commentsDto){
        this.modelMapper = new ModelMapper();
        TypeMap<CommentsDto, Comment> propertyMapper = modelMapper.createTypeMap(CommentsDto.class, Comment.class);

        Instant createdTime = java.time.Instant.now();

        propertyMapper.addMappings(
                mapper -> mapper.skip(Comment::setCommentId)
        );
        propertyMapper.addMappings(
                mapper -> mapper.map(src -> commentsDto.getText(), Comment::setText)
        );
        propertyMapper.addMappings(
                mapper -> mapper.map( src->  createdTime, Comment::setCreatedDate)
        );
        propertyMapper.addMappings(
                mapper -> mapper.map( src->  postRepository.findById(commentsDto.getPostId()), Comment::setPost)
        );
        propertyMapper.addMappings(
                mapper -> mapper.map( src->  authService.getCurrentUser(), Comment::setUser)
        );

        Comment comment = modelMapper.map(commentsDto, Comment.class);
        return comment;

    }

//    public CommentsDto MapCommentToDto(Comment comment){
//        CommentsDto commentsDto = modelMapper.map(comment, CommentsDto.class);
//        return commentsDto;
//    }
//
//    public Comment MapDtoToComment(CommentsDto commentsDto){
//        Comment comment = modelMapper.map(commentsDto, Comment.class);
//        return comment;
//    }

}
