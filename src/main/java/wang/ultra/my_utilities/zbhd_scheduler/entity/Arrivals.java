package wang.ultra.my_utilities.zbhd_scheduler.entity;

import lombok.Data;

/**
 * 抵达
 */
@Data
public class Arrivals {
    private String flightNo;    // 航班号
    private String airportICAO; // 机场ID
    private String departure;   // 起飞时间
    private String arrival;     // 降落时间
    private String schedule;    // 班期
    private String aircraft;    // 机型
    private String updateDate;
    private int status;
}
