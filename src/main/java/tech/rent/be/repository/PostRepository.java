package tech.rent.be.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.rent.be.entity.Post;
import tech.rent.be.entity.RealEstate;
import tech.rent.be.entity.Users;

import java.util.List;

public interface PostRepository extends JpaRepository<Post,Long> {
    Post findById(long id);
    List<Post> findPostsByUsers(Users user);
}