package wang.ultra.my_utilities.stock_exchange.entity;

import lombok.Data;

@Data
public class NotifyEntity extends BaseEntity {
    private String indicatorTime;
    private String notifyType;
    private String notifyId;
    private String recordTime;
    private String msg;
}
