package life.java.community.dto;

import lombok.Data;

//登陆信息
@Data
public class AccesstokenDto {
    private String client_id;
    private String client_secret;
    private String code;
    private String redirect_uri;
    private String state;
}
