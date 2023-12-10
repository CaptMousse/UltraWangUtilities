package wang.ultra.my_utilities.attached_storage.entity.dto;

import lombok.Data;
import wang.ultra.my_utilities.attached_storage.entity.FileEntity;

@Data
public class FileDto extends FileEntity {
    private String createStartTime;
    private String createEndTime;
    private String updateStartTime;
    private String updateEndTime;
    private String lastAccessStartTime;
    private String lastAccessEndTime;
}
