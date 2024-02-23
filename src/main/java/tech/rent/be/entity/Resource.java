package tech.rent.be.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import tech.rent.be.enums.Type;
@Entity
public class Resource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    Type resource;
    String url;

    @ManyToOne
    @JsonProperty(JsonProperty.Access.READ_ONLY)
    @JoinColumn(name = "post_id")
    Post post;

    @ManyToOne
    @JoinColumn(name = "estate_id")
    RealEstate realEstate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public RealEstate getRealEstate() {
        return realEstate;
    }

    public void setRealEstate(RealEstate realEstate) {
        this.realEstate = realEstate;
    }
}
