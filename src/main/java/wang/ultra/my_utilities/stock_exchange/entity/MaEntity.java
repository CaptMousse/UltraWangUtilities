package wang.ultra.my_utilities.stock_exchange.entity;

import lombok.Data;

@Data
public class MaEntity extends BaseEntity {
    private String ma3;
    private String ma5;
    private String ma10;
    private String ma15;
    private String ma20;
    private String ma30;
    private String ma60;
    private String ma120;
    private String ma200;
    private String ma250;
}
