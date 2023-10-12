package wang.ultra.my_utilities.stock_exchange.entity;

import lombok.Data;

@Data
public class NotifyMaEntity {
    private String id;
    private String brief;
    private String price;
    private String ma5;
    private String ma10;
}
