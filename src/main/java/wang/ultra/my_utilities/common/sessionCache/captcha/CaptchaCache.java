package wang.ultra.my_utilities.common.sessionCache.captcha;

import lombok.Data;

@Data
public class CaptchaCache {
    private String uuid;
    private String captcha;
    private long expireMillis;

    public CaptchaCache(String uuid, String captcha, long expireMillis) {
        this.uuid = uuid;
        this.captcha = captcha;
        this.expireMillis = expireMillis;
    }
}
