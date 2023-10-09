package wang.ultra.my_utilities.stock_exchange.mapper;

import org.apache.ibatis.annotations.Mapper;
import wang.ultra.my_utilities.stock_exchange.entity.MaEntity;
import wang.ultra.my_utilities.stock_exchange.entity.MacdEntity;

import java.util.List;
import java.util.Map;

@Mapper
public interface TradingDataMapper {

    void macdAdd(List<MacdEntity> list);

    void maAdd(List<MaEntity> list);

    List<Map<String, Object>> getStockSyncList();
}
