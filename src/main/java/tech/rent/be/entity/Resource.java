package tech.rent.be.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import tech.rent.be.enums.Type;
@Entity
@Data
public class Resource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Enumerated(EnumType.STRING)
    Type resourceType;
    String url;

    @ManyToOne
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JoinColumn(name = "post_id")
            @JsonIgnore
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

    public Type getResourceType() {
        return resourceType;
    }

    public void setResourceType(Type resourceType) {
        this.resourceType = resourceType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
