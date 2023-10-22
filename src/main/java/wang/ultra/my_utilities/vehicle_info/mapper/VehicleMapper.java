package wang.ultra.my_utilities.vehicle_info.mapper;

import org.apache.ibatis.annotations.Mapper;
import wang.ultra.my_utilities.vehicle_info.entity.BrandEntity;
import wang.ultra.my_utilities.vehicle_info.entity.ModelEntity;
import wang.ultra.my_utilities.vehicle_info.entity.YearEntity;

import java.util.List;

@Mapper
public interface VehicleMapper {

    void addBrand(List<BrandEntity> entityList);

    void addYear(List<YearEntity> entityList);

    void addModel(List<ModelEntity> entityList);
}
