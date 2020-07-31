package life.java.community.dto;

import lombok.Data;

//github信息
@Data
public class GithubUser {
    private String name;
    private Long id;
    private String bio;
    private String avatarUrl;
}
