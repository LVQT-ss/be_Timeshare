package tech.rent.be.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.rent.be.dto.PostRequestDTO;
import tech.rent.be.dto.PostResponseDTO;
import tech.rent.be.dto.UserDTO;
import tech.rent.be.entity.Post;
import tech.rent.be.services.PostService;

import java.util.List;

@RestController
@RequestMapping("/post")
@SecurityRequirement(name = "api")
@CrossOrigin("*")
public class PostController {
    @Autowired
    PostService postService;
    @PostMapping
    public ResponseEntity post(@RequestBody PostRequestDTO postRequestDTO){
        Post post = postService.createPost(postRequestDTO);
        return  ResponseEntity.ok(post);
    }
    @GetMapping("/show")
    public ResponseEntity<List<PostResponseDTO>> getAllPosts() {
        List<PostResponseDTO> posts = postService.getAllPosts();
        if (!posts.isEmpty()) {
            return ResponseEntity.ok(posts);
        } else {
            return ResponseEntity.noContent().build();
        }
    }
}