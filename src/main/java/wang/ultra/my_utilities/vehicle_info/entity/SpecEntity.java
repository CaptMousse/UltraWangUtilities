package wang.ultra.my_utilities.vehicle_info.entity;

import lombok.Data;

import java.util.List;

@Data
public class SpecEntity extends BaseEntity {
    private String modelId;
    private String body;
    private String frontAxisBoltPattern;
    private String frontAxisCentreBore;
    private String engineCode;
    private String engineType;
    private String fuel;
    private String generationName;
    private String generationStartYear;
    private String generationEndYear;
    private String generationYears;
    private List<SpecBodiesEntity> bodiesList;
    private String lockText;
    private String lockType;
    private String marketId;
    private String marketAbbr;
    private String marketName;
    private String frontAxisPcd;
    private String powerPs;
    private String powerHp;
    private String powerKw;
    private String rearAxisBoltPattern;
    private String rearAxisCentreBore;
    private String rearAxisPcd;
    private String rearAxisStudHoles;
    private String frontAxisStudHoles;
    private String trim;
    private String trimAttributes;
    private String trimBodyTypes;
    private List<SpecWheelsEntity> wheelsList;
}
