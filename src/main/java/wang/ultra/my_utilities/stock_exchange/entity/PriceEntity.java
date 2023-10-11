package wang.ultra.my_utilities.stock_exchange.entity;

import lombok.Data;

@Data
public class PriceEntity {
    private String id;
    private String stockId;
    private String indicatorDate;
    private String kpj; // 开盘价
    private String zgj; // 最高价
    private String zdj; // 最低价
    private String spj; // 收盘价
    private String cjl; // 成交量
    private String cje; // 成交额
    private String zf;  // 振幅
    private String hsl; // 换手率
    private String zdf; // 涨跌幅
    private String zde; // 涨跌额
}
