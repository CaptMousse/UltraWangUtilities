package wang.ultra.my_utilities.attached_storage.entity;

import lombok.Data;

@Data
public class FolderEntity {
    private String folderId;
    private String folderName;
    private String parentId;
    private String creator;
    private String createTime;
    private String status;
}
