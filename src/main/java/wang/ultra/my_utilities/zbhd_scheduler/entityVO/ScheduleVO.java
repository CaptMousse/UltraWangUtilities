package wang.ultra.my_utilities.zbhd_scheduler.entityVO;

import lombok.Data;

@Data
public class ScheduleVO {
    private String schedule;

    public ScheduleVO(String schedule) {
        this.schedule = schedule;
    }
}
