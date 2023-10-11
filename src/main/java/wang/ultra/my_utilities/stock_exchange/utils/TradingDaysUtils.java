package wang.ultra.my_utilities.stock_exchange.utils;

import wang.ultra.my_utilities.common.utils.DateConverter;

public class TradingDaysUtils {

    public static boolean getTradingDay() {
        int weekDayNum = DateConverter.getWeekDayNum();
        return weekDayNum != 7 && weekDayNum != 1;
    }
}
