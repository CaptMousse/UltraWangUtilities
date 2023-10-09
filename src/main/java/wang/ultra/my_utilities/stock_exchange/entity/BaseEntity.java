package wang.ultra.my_utilities.stock_exchange.entity;

import lombok.Data;

@Data
public class BaseEntity {
    private String id;
    private String stockId;
    private String indicatorDate;
}
