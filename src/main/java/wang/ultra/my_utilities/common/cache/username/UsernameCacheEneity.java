package wang.ultra.my_utilities.common.cache.username;

import lombok.Data;

@Data
public class UsernameCacheEneity {
    private String uuid;
    private String username;
    private long expireMillis;

    public UsernameCacheEneity(String uuid, String username, long expireMillis) {
        this.uuid = uuid;
        this.username = username;
        this.expireMillis = expireMillis;
    }
}
