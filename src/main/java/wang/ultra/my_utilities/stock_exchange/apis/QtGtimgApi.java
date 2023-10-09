package wang.ultra.my_utilities.stock_exchange.apis;

import wang.ultra.my_utilities.common.utils.StringUtils;
import wang.ultra.my_utilities.stock_exchange.utils.UrlConnectionUtils;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 腾讯股票接口API
 * 注意, stockId要加上交易所标识
 * 例如江苏银行是sz600919, 京东方是sz000725
 */
public class QtGtimgApi {

    private final String stockId;

    private final String key = """
            交易所
            股票名字
            股票代码
            当前价格
            昨收
            开盘
            成交量
            外盘
            内盘
            买1
            买1量
            买2
            买2量
            买3
            买3量
            买4
            买4量
            买5
            买5量
            卖1
            卖1量
            卖2
            卖2量
            卖3
            卖3量
            卖4
            卖4量
            卖5
            卖5量
            -
            请求时间
            涨跌
            涨跌%
            最高
            最低
            最新价/成交量(手)/成交额
            成交量
            成交额
            换手率
            TTM市盈率
            -
            均价
            动态市盈率
            静态市盈率
            -
            -
            -
            成交额""";

    public QtGtimgApi(String stockId) {
        this.stockId = stockId;
    }

    public String getStockNow() {
        String url = "https://qt.gtimg.cn/q=" + stockId;
        String responseStockNow;
        try {
            responseStockNow = UrlConnectionUtils.getConnection(url, "GET", "GB2312");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return responseStockNow;
    }

    public static void main(String[] args) {
        System.out.println(StringUtils.getMyUUID());
    }
}
