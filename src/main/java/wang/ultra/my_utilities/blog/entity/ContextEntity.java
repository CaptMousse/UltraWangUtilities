package wang.ultra.my_utilities.blog.entity;

import lombok.Data;

@Data
public class ContextEntity {

    private String uuid;
    private String title;
    private String user;
    private String create_time;
    private String update_time;
    private Integer status;
}
