package wang.ultra.my_utilities.common.cache.stockData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StockMaCacheList {
    private static final List<Map<String, String>> stockMaCacheList = new ArrayList<>();

    public void add(Map<String, String> stockMaMap) {
        stockMaCacheList.add(stockMaMap);
    }

    public Map<String, String> get(String stockId) {
        for (Map<String, String> map : stockMaCacheList) {
            String id = map.get("stockId");
            if (stockId.equals(id)) {
                return map;
            }
        }
        return null;
    }

    public List<Map<String, String>> getAll() {
        return stockMaCacheList;
    }

    public void clean() {
        stockMaCacheList.clear();
    }
}
