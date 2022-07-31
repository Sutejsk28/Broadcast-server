package com.sutej.broadcast.mapper;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.sutej.broadcast.dto.PostRequest;
import com.sutej.broadcast.dto.PostResponse;
import com.sutej.broadcast.modals.Post;
import com.sutej.broadcast.modals.Vote;
import com.sutej.broadcast.modals.VoteType;
import com.sutej.broadcast.repository.CommentRepository;
import com.sutej.broadcast.repository.SubcastRepository;
import com.sutej.broadcast.repository.VoteRepository;
import com.sutej.broadcast.services.AuthService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;

import static com.sutej.broadcast.modals.VoteType.DOWNVOTE;
import static com.sutej.broadcast.modals.VoteType.UPVOTE;

@Component
public class PostMapper {

//    SubcastRepository subcastRepository;
//    UserRepository userRepository;
//
//    public PostResponse mapPosttoDto(Post post){
//        PostResponse postResponse = new PostResponse();
//
//        postResponse.setId(post.getPostId());
//        postResponse.setUserName(post.getUser().getUserName());
//        postResponse.setSubrcastName(post.getSubCast().getSubcastName());
//        postResponse.setUrl(post.getUrl());
//        postResponse.setDescription(post.getDescription());
//
//        return postResponse;
//    }
//
//    public Post mapDtoToPost(PostResponse postResponse){
//        return Post.builder()
//                .postId(postResponse.getId())
//                .postName(postResponse.getPostName())
//                .subCast(subcastRepository.findBysubcastName(postResponse.getSubrcastName()))
//                .description(postResponse.getDescription())
//                .user(userRepository.findByuserName(postResponse.getUserName())).build();
//    }

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private VoteRepository voteRepository;
    @Autowired
    private AuthService authService;
    @Autowired
    private SubcastRepository subcastRepository;

    @Autowired
    private ModelMapper modelMapper;

    public PostResponse MapToDto(Post post){

        this.modelMapper = new ModelMapper();

        TypeMap<Post, PostResponse> propertyMapper = modelMapper.createTypeMap(Post.class, PostResponse.class);

        propertyMapper.addMappings(
            mapper -> mapper.map(src -> post.getPostId(), PostResponse::setId)
        );
        propertyMapper.addMappings(
            mapper -> mapper.map(src -> src.getSubCast().getSubcastName(), PostResponse::setSubcastName)
        );
        propertyMapper.addMappings(
                mapper -> mapper.map(src -> src.getUser().getUserName(), PostResponse::setUserName)
        );
        propertyMapper.addMappings(
                mapper -> mapper.map(src -> commentCount(post), PostResponse::setCommentCount)
        );
        propertyMapper.addMappings(
                mapper -> mapper.map(src -> getDuration(post), PostResponse::setDuration)
        );
        propertyMapper.addMappings(
                mapper -> mapper.map(src -> isPostUpVoted(post), PostResponse::setUpVote)
        );
        propertyMapper.addMappings(
                mapper -> mapper.map(src -> isPostDownVoted(post), PostResponse::setDownVote)
        );
        propertyMapper.addMappings(
                mapper -> mapper.map(src -> src.getVoteCount(), PostResponse::setVoteCount)
        );

        PostResponse postResponse = modelMapper.map(post, PostResponse.class);
        modelMapper.validate();
        return postResponse;
    }


    public Post mapDtoToPost(PostRequest postRequest){

        this.modelMapper = new ModelMapper();

        TypeMap<PostRequest, Post> propertyMapperDto = modelMapper.createTypeMap(PostRequest.class, Post.class);

        Instant currentTime = java.time.Instant.now();

        propertyMapperDto.addMappings(
                mapper -> mapper.map(src -> currentTime, Post::setPostCreated)
        );
        propertyMapperDto.addMappings(
                mapper -> mapper.map(src -> src.getDescription(), Post::setDescription)
        );
        propertyMapperDto.addMappings(
                mapper -> mapper.map(src -> subcastRepository.findBysubcastName(src.getSubcastName()), Post::setSubCast)
        );
        propertyMapperDto.addMappings(
                mapper -> mapper.map(src -> 0, Post::setVoteCount)
        );
        propertyMapperDto.addMappings(
                mapper -> mapper.map(src -> authService.getCurrentUser(), Post::setUser)
        );

        Post post = modelMapper.map(postRequest, Post.class);
        return post;

    }

    Integer commentCount(Post post) {
        return commentRepository.findByPost(post).size();
    }

    String getDuration(Post post) {
        return TimeAgo.using(post.getPostCreated().toEpochMilli());
    }

    boolean isPostUpVoted(Post post) {
        return checkVoteType(post, UPVOTE);
    }

    boolean isPostDownVoted(Post post) {
        return checkVoteType(post, DOWNVOTE);
    }

    private boolean checkVoteType(Post post, VoteType voteType) {
        if (authService.isLoggedIn()) {
            Optional<Vote> voteForPostByUser =
                    voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post,
                            authService.getCurrentUser());
            return voteForPostByUser.filter(vote -> vote.getVoteType().equals(voteType))
                    .isPresent();
        }
        return false;
    }



//    public PostResponse mapPostToDto(Post post) {
//        PostResponse postResponse = modelMapper.map(post, PostResponse.class);
//        return postResponse;
//    }
//
//    public Post mapDtoToPost(PostRequest postRequest){
//        Post post = modelMapper.map(postRequest, Post.class);
//        return post;
//    }

}
