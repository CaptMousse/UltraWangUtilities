package wang.ultra.my_utilities.stock_exchange.entity;

import lombok.Data;

@Data
public class MacdEntity extends BaseEntity{
    private String dea;
    private String diff;
    private String macd;
}
