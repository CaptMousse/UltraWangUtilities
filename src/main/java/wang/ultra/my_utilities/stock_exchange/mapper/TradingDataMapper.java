package wang.ultra.my_utilities.stock_exchange.mapper;

import org.apache.ibatis.annotations.Mapper;
import wang.ultra.my_utilities.stock_exchange.entity.MaEntity;
import wang.ultra.my_utilities.stock_exchange.entity.MacdEntity;
import wang.ultra.my_utilities.stock_exchange.entity.PriceEntity;
import wang.ultra.my_utilities.stock_exchange.entity.StockEntity;

import java.util.List;
import java.util.Map;

@Mapper
public interface TradingDataMapper {

    void stockAdd(List<StockEntity> list);

    void priceAdd(List<PriceEntity> list);
    void macdAdd(List<MacdEntity> list);

    void maAdd(List<MaEntity> list);

    List<Map<String, Object>> getStockSyncList();
    List<Map<String, Object>> getStock(String stockId);

    void updateSyncStatus(String stockId);
    void updateSyncOn(String stockId);
    void updateSyncOff(String stockId);
    List<Map<String, Object>> getAbstractMa(String stockId, String today);
    List<Map<String, Object>> getYesterdayMa(String stockId, String today);
}
