package tech.rent.be.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.rent.be.dto.PostRequestDTO;
import tech.rent.be.dto.PostResponseDTO;
import tech.rent.be.dto.ResourceDTO;
import tech.rent.be.entity.Post;
import tech.rent.be.entity.Resource;
import tech.rent.be.entity.Users;
import tech.rent.be.enums.PostStatus;
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

        List<Resource> resources = new ArrayList<>();

        // ResourceDTO => Resource
        for(ResourceDTO resourceDTO: postRequestDTO.getResources()){
            Resource resource = new Resource();
            resource.setResourceType(resourceDTO.getResourceType());
            resource.setUrl(resourceDTO.getUrl());
            resource.setPost(post);
            resources.add(resource);
        }
        post.setResource(resources);
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

            List<ResourceDTO> resourceDTOS = new ArrayList<>();
            // ResourceDTO => Resource
            for(Resource resource:  post.getResource()){
                ResourceDTO resourceDTO = new ResourceDTO();
                resourceDTO.setResourceType(resource.getResourceType());
                resourceDTO.setUrl(resource.getUrl());
                resourceDTOS.add(resourceDTO);
            }
            postResponseDTO.setResources(resourceDTOS);

        }

        return postResponseDTOList;
    }
    public Post deletePost(long postId) {
       Post post = postRepository.findById(postId);
       post.setPostStatus(PostStatus.INACTIVE);
       return  postRepository.save(post);
    }

}

