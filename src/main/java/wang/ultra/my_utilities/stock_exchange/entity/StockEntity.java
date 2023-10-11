package wang.ultra.my_utilities.stock_exchange.entity;

import lombok.Data;

@Data
public class StockEntity {
    private String stockId;
    private String stockName;
    private String exchangeId;
    private String ifSync;
}
