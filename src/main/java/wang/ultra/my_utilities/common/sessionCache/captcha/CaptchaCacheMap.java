package wang.ultra.my_utilities.common.sessionCache.captcha;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 存储验证码的Map
 */
public class CaptchaCacheMap {

    private static final Map<String, CaptchaCache> cacheMap = new HashMap<>();

    public CaptchaCacheMap() {
        // 清理过期的验证码
        long cleanMillis = System.currentTimeMillis() - 60000;
        // 不能用for遍历删除符合条件的验证码
        Iterator<Map.Entry<String, CaptchaCache>> iterator = cacheMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, CaptchaCache> entry = iterator.next();
            CaptchaCache captchaCache = entry.getValue();
            long captchaMillis = captchaCache.getExpireMillis();
            if (captchaMillis < cleanMillis) {
                iterator.remove();
            }
        }
    }

    public CaptchaCacheMap(String uuid, String captcha) {
        long expireMillis = System.currentTimeMillis();
        CaptchaCache captchaCache = new CaptchaCache(uuid, captcha, expireMillis);
        cacheMap.put(uuid, captchaCache);
    }

    public Map<String, CaptchaCache> getAll() {
        return cacheMap;
    }


    public String getCaptcha(String uuid) {
        return cacheMap.get(uuid).getCaptcha();
    }

    public void setCaptcha(String uuid, String captcha) {
        long expireMillis = System.currentTimeMillis();
        CaptchaCache captchaCache = new CaptchaCache(uuid, captcha, expireMillis);
        cacheMap.put(uuid, captchaCache);
    }
}
