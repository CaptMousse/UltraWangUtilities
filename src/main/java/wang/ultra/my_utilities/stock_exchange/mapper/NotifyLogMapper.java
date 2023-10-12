package wang.ultra.my_utilities.stock_exchange.mapper;

import org.apache.ibatis.annotations.Mapper;
import wang.ultra.my_utilities.stock_exchange.entity.NotifyEntity;
import wang.ultra.my_utilities.stock_exchange.entity.NotifyMaEntity;

import java.util.List;
import java.util.Map;

@Mapper
public interface NotifyLogMapper {
    void notifyAdd(List<NotifyEntity> list);

    void notifyMaAdd(List<NotifyMaEntity> list);

    List<Map<String, Object>> getTodayNotifyMaRecordByStockId(String stockId, String indicatorDate);
}
