package tech.rent.be.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(columnDefinition = "nvarchar(255)")
    String content;

    @Column(columnDefinition = "nvarchar(255)")
    String title;

    Long Price;
    Date PostDate;


    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    Users users;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    List<Resource> resource;

    public List<Resource> getResource() {
        return resource;
    }

    public void setResource(List<Resource> resource) {
        this.resource = resource;
    }

    public void setUser(Users users) {
        this.users = users;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getPrice() {
        return Price;
    }

    public void setPrice(Long price) {
        Price = price;
    }

    public Date getPostDate() {
        return PostDate;
    }

    public void setPostDate(Date postDate) {
        PostDate = postDate;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }
}