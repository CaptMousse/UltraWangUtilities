package wang.ultra.my_utilities.common.cache.stockData;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class StockNowPriceCacheList {

    private static final List<Map<String, String>> stockNowPriceCacheList = new ArrayList<>();

    public void add(Map<String, String> stockMap) {

        String id = stockMap.get("股票代码");

        // 获得CacheList下标位置, 更新Map
        for (int i = 0; i < stockNowPriceCacheList.size(); i++) {
            Map<String, String> cacheMap = stockNowPriceCacheList.get(i);
            if (id.equals(cacheMap.get("股票代码"))) {
                stockNowPriceCacheList.set(i, stockMap);
                return; // 如果更新完就return
            }
        }
        // 没找到下标就添加
        stockNowPriceCacheList.add(stockMap);
    }

    public void remove(String id) {
        stockNowPriceCacheList.removeIf(map -> id.equals(map.get("股票代码")));
        // 上面是下面的简写
//        Iterator<Map<String, String>> iterator = stockNowPriceCacheList.iterator();
//        while (iterator.hasNext()) {
//            Map<String, String> map = iterator.next();
//            if (id.equals(map.get("股票代码"))) {
//                iterator.remove();
//            }
//        }

        // 会报ConcurrentModificationException
//        for (int i = 0; i < stockNowPriceCacheList.size(); i++) {
//            Map<String, String> cacheMap = stockNowPriceCacheList.get(i);
//            if (id.equals(cacheMap.get("股票代码"))) {
//                stockNowPriceCacheList.remove(i);
//                return; // 如果更新完就return
//            }
//        }
    }

    public Map<String, String> get(String stockId) {
        for (Map<String, String> map : stockNowPriceCacheList) {
            String id = map.get("股票代码");
            if (stockId.equals(id)) {
                return map;
            }
        }
        return null;
    }

    public List<Map<String, String>> getAll() {
        return stockNowPriceCacheList;
    }

    public void clean() {
        stockNowPriceCacheList.clear();
    }
}
