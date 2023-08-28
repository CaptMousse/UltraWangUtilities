package wang.ultra.my_utilities.common.cache.captcha;

import lombok.Data;

@Data
public class CaptchaCacheEntity {
    private String uuid;
    private String captcha;
    private long expireMillis;

    public CaptchaCacheEntity(String uuid, String captcha, long expireMillis) {
        this.uuid = uuid;
        this.captcha = captcha;
        this.expireMillis = expireMillis;
    }
}
