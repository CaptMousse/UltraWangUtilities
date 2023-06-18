package wang.ultra.my_utilities.common.download.entity;

import lombok.Data;

@Data
public class FileTransferEntity {
    private String id;
    private String real_name;
    private String show_name;
    private String create_time;
    private String creator;
    private int status;
}
