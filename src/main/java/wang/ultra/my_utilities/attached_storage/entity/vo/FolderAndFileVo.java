package wang.ultra.my_utilities.attached_storage.entity.vo;

import lombok.Data;

@Data
public class FolderAndFileVo {

    private String id;
    private String name;
    private String size;
    private String lastAccessTime;
    private String creator;
    private String createTime;
    private String updateTime;
    private String type;
}
