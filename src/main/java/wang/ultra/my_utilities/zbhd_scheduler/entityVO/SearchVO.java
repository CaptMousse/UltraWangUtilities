package wang.ultra.my_utilities.zbhd_scheduler.entityVO;

import lombok.Data;

@Data
public class SearchVO {
    private String flightNo;    // 航班号
    private String airportICAO; // ICAO代码 ZSNJ
    private String IATA;        // IATA代码 NKG
    private String name;
    private String city;
}
