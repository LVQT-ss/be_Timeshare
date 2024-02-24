package tech.rent.be.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.rent.be.dto.PostRequestDTO;
import tech.rent.be.dto.PostResponseDTO;
import tech.rent.be.entity.Post;
import tech.rent.be.entity.Users;
import tech.rent.be.repository.PostRepository;
import tech.rent.be.repository.UsersRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {

    @Autowired
    PostRepository postRepository;
    @Autowired
    UsersRepository usersRepository;

    public Post createPost(PostRequestDTO postRequestDTO){
        Users users = usersRepository.findUsersById(postRequestDTO.getUserId());
        Post post = new Post();
        post.setId(postRequestDTO.getId());
        post.setContent(postRequestDTO.getContent());
        post.setTitle(postRequestDTO.getTitle());
        post.setPrice(postRequestDTO.getPrice());
        post.setPostDate(postRequestDTO.getPostDate());
        post.setUser(users);
        return postRepository.save(post);

    }
    public List<PostResponseDTO> getAllPosts() {
        List<Post> postList = postRepository.findAll();
        List<PostResponseDTO> postResponseDTOList = new ArrayList<>();
        for (Post post : postList) {
            PostResponseDTO postResponseDTO = new PostResponseDTO();
            // Map fields from post to postResponseDTO
            postResponseDTO.setId(post.getId());
            postResponseDTO.setContent(post.getContent());
            postResponseDTO.setTitle(post.getTitle());
            postResponseDTO.setPrice(post.getPrice());
            postResponseDTO.setPostDate(post.getPostDate());
            postResponseDTOList.add(postResponseDTO);
        }
        return postResponseDTOList;
    }
}

