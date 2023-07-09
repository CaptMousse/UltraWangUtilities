package wang.ultra.my_utilities.blog.entity;

import lombok.Data;

@Data
public class UserLoginEntity {
    private String username;
    private String password;
    private String salt;
    private String create_time;
    private String update_time;
    private int status;

    public UserLoginEntity(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
