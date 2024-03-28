package tech.rent.be.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.rent.be.dto.PostRequestDTO;
import tech.rent.be.dto.PostResponseDTO;
import tech.rent.be.dto.RealEstateDTO;
import tech.rent.be.dto.ResourceDTO;
import tech.rent.be.entity.Post;
import tech.rent.be.entity.RealEstate;
import tech.rent.be.entity.Resource;
import tech.rent.be.entity.Users;
import tech.rent.be.enums.PostStatus;
import tech.rent.be.repository.PostRepository;
import tech.rent.be.repository.RealEstateRepository;
import tech.rent.be.repository.UsersRepository;
import tech.rent.be.utils.AccountUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {

    @Autowired
    PostRepository postRepository;
    @Autowired
    UsersRepository usersRepository;

    @Autowired
    RealEstateRepository realEstateRepository;
    @Autowired
    AccountUtils accountUtils;

    public Post createPost(PostRequestDTO postRequestDTO) {

        Users users = accountUtils.getCurrentUser();
        RealEstate realEstate = realEstateRepository.findRealEstateById(postRequestDTO.getEstateId());
        Post post = new Post();
        post.setPostStatus(PostStatus.ACTIVE);
        post.setThumbnail(postRequestDTO.getThumbnail());
        post.setFromDay(postRequestDTO.getFromDay());
        post.setToDay(postRequestDTO.getToDay());
        post.setDiscount(postRequestDTO.getDiscount());
        post.setUsers(users);
        post.setRealEstate(realEstate);
        return postRepository.save(post);
    }

    public List<PostResponseDTO> getAllPosts() {
        List<Post> postList = postRepository.findAll();
        List<PostResponseDTO> postResponseDTOList = new ArrayList<>();
        for (Post post : postList) {
            RealEstate realEstate = post.getRealEstate();
            RealEstateDTO realEstateDTO = new RealEstateDTO();
            // Map the properties from the real estate entity to the DTO
            realEstateDTO.setId(realEstate.getId());
            realEstateDTO.setTitle(realEstate.getTitle());
            realEstateDTO.setDescription(realEstate.getDescription());
            realEstateDTO.setAmount(realEstate.getAmount());
            realEstateDTO.setCheckIn(realEstate.getCheckIn());
            realEstateDTO.setCheckOut(realEstate.getCheckOut());

            realEstateDTO.setPrice(realEstate.getPrice());
            // Map category and location details
            realEstateDTO.setCategory(realEstate.getCategory().getCategoryname());
            realEstateDTO.setCategoryId(realEstate.getCategory().getId());
            realEstateDTO.setLocation(realEstate.getLocation().getLocation());
            realEstateDTO.setLocationId(realEstate.getLocation().getId());
            PostResponseDTO postResponseDTO = new PostResponseDTO();
            // Map fields from post to postResponseDTO
            postResponseDTO.setId(post.getId());
            postResponseDTO.setFromDay(post.getFromDay());
            postResponseDTO.setToDay(post.getToDay());
            postResponseDTO.setDiscount(post.getDiscount());
            postResponseDTO.setRealEstate(realEstateDTO);
            postResponseDTO.setThumbnail(post.getThumbnail());
            postResponseDTOList.add(postResponseDTO);
        }
        return postResponseDTOList;
    }

    public Post deletePost(long postId) {
        Post post = postRepository.findById(postId);
        post.setPostStatus(PostStatus.INACTIVE);
        return postRepository.save(post);
    }

    public List<PostResponseDTO> getAllPostsByCurrentUser() {
        Users currentUser = accountUtils.getCurrentUser();
        if (currentUser == null) {
            // Handle the case where the user is not foundq
            return null;
        }
        List<Post> userPost = postRepository.findPostsByUsers(currentUser);
        if (userPost.isEmpty()) {
            // Handle the case where the user has not posted any real estate properties
            return null;
        }
        List<PostResponseDTO> postResponseDTOForCurrentUser = new ArrayList<>();
        for (Post post : userPost) {
            RealEstate realEstate = post.getRealEstate();
            RealEstateDTO realEstateDTO = new RealEstateDTO();
            // Map the properties from the real estate entity to the DTO
            realEstateDTO.setId(realEstate.getId());
            realEstateDTO.setTitle(realEstate.getTitle());
            realEstateDTO.setDescription(realEstate.getDescription());
            realEstateDTO.setAmount(realEstate.getAmount());
            realEstateDTO.setCheckIn(realEstate.getCheckIn());
            realEstateDTO.setCheckOut(realEstate.getCheckOut());

            realEstateDTO.setPrice(realEstate.getPrice());
            // Map category and location details
            realEstateDTO.setCategory(realEstate.getCategory().getCategoryname());
            realEstateDTO.setCategoryId(realEstate.getCategory().getId());
            realEstateDTO.setLocation(realEstate.getLocation().getLocation());
            realEstateDTO.setLocationId(realEstate.getLocation().getId());

            PostResponseDTO postResponseDTO = new PostResponseDTO();
            postResponseDTO.setThumbnail(post.getThumbnail());
            postResponseDTO.setId(post.getId());
            postResponseDTO.setDiscount(post.getDiscount());
            postResponseDTO.setRealEstate(realEstateDTO);
            postResponseDTOForCurrentUser.add(postResponseDTO);

        }

        return postResponseDTOForCurrentUser;

    }

    public Post getPostByID(long id) {
        return postRepository.findById(id);
    }
}

