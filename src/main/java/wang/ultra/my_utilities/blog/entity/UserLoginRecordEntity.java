package wang.ultra.my_utilities.blog.entity;

import lombok.Data;

@Data
public class UserLoginRecordEntity {
    private String uuid;
    private String username;
    private String record_time;
    private int record_type;
    private String record_context;  // 登入说明或登出说明, 例如单点登录或者到期登出
    private String login_ip;

    public UserLoginRecordEntity(String username) {
        this.username = username;
    }

    /**
     * 登出
     * @param username
     * @param record_context    记录说明
     */
    public UserLoginRecordEntity(String username, String record_context) {
        this.username = username;
        this.record_context = record_context;
    }


    /**
     * 登入
     * @param username
     * @param record_context    记录说明
     * @param login_ip
     */
    public UserLoginRecordEntity(String username, String record_context, String login_ip) {
        this.username = username;
        this.record_context = record_context;
        this.login_ip = login_ip;
    }
}
