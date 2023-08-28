package wang.ultra.my_utilities.common.cache.captcha;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 存储验证码的Map
 */
public class CaptchaCacheMap {

    private static final Map<String, CaptchaCacheEntity> cacheMap = new HashMap<>();

    public CaptchaCacheMap() {
        // 清理过期的验证码
        long cleanMillis = System.currentTimeMillis() - 60000;
        // 不能用for遍历删除符合条件的验证码
        Iterator<Map.Entry<String, CaptchaCacheEntity>> iterator = cacheMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, CaptchaCacheEntity> entry = iterator.next();
            CaptchaCacheEntity captchaCacheEntity = entry.getValue();
            long captchaMillis = captchaCacheEntity.getExpireMillis();
            if (captchaMillis < cleanMillis) {
                iterator.remove();
            }
        }
    }

    public CaptchaCacheMap(String uuid, String captcha) {
        long expireMillis = System.currentTimeMillis();
        CaptchaCacheEntity captchaCacheEntity = new CaptchaCacheEntity(uuid, captcha, expireMillis);
        cacheMap.put(uuid, captchaCacheEntity);
    }

    public Map<String, CaptchaCacheEntity> getAll() {
        return cacheMap;
    }


    public String getCaptcha(String uuid) {
        return cacheMap.get(uuid).getCaptcha();
    }

    public void setCaptcha(String uuid, String captcha) {
        long expireMillis = System.currentTimeMillis();
        CaptchaCacheEntity captchaCacheEntity = new CaptchaCacheEntity(uuid, captcha, expireMillis);
        cacheMap.put(uuid, captchaCacheEntity);
    }
}
