package wang.ultra.my_utilities.attached_storage.entity;

import lombok.Data;

@Data
public class FileEntity {
    private String realName;
    private String showName;
    private String fileSize;
    private String fileMd5;
    private String folderId;
    private String creator;
    private String createTime;
    private String updateTime;
    private String amount;
    private String lastAccessTime;
    private String status;

    public FileEntity(){

    }

    public FileEntity(String realName) {
        this.realName = realName;
    }
}
