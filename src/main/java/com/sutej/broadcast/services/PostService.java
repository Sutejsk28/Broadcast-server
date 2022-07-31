package com.sutej.broadcast.services;

import com.sutej.broadcast.dto.PostRequest;
import com.sutej.broadcast.dto.PostResponse;
import com.sutej.broadcast.exception.PostNotFoundException;
import com.sutej.broadcast.exception.SubcastNotFoundException;
import com.sutej.broadcast.mapper.PostMapper;
import com.sutej.broadcast.modals.Post;
import com.sutej.broadcast.modals.SubCast;
import com.sutej.broadcast.modals.User;
import com.sutej.broadcast.repository.PostRepository;
import com.sutej.broadcast.repository.SubcastRepository;
import com.sutej.broadcast.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class PostService {

    private final SubcastRepository subcastRepository;
    private final AuthService authService;
    private final PostMapper postMapper;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public Post save(PostRequest postRequest) {
        subcastRepository.findBysubcastName(postRequest.getSubcastName())
                .orElseThrow( ()->new SubcastNotFoundException(postRequest.getSubcastName()) );
        return postRepository.save(postMapper.mapDtoToPost(postRequest));
    }

    @Transactional(readOnly = true)
    public PostResponse getPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow( ()-> new PostNotFoundException(id.toString()));
        return postMapper.MapToDto(post);
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getAllPosts() {
        return postRepository.findAll()
                .stream()
                .map(postMapper::MapToDto)
                .collect(Collectors.toList());
    }

    public List<PostResponse> getPostBySubcast(Long subcastId) {
        SubCast subCast = subcastRepository.findById(subcastId)
                .orElseThrow( ()-> new SubcastNotFoundException(subcastId.toString()) );
        List<Post> posts = postRepository.findAllBysubCast(subCast);
        return posts.stream().map(postMapper::MapToDto).collect(Collectors.toList());
    }

    public List<PostResponse> getPostsByUsername(String username) {
        User user = userRepository.findByuserName(username)
                .orElseThrow( ()-> new UsernameNotFoundException(username));
        return postRepository.findByUser(user)
                .stream()
                .map(postMapper::MapToDto)
                .collect(Collectors.toList());
    }
}
