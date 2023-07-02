package wang.ultra.my_utilities.aliyun_ddns_update.service;


import wang.ultra.my_utilities.common.constant.ConstantFromFile;
import wang.ultra.my_utilities.common.utils.FileIOUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class LogIOService {

    /**
     * 获得日志中IPv6记录信息的时间
     * @param method    1 = 只需要获得时间, 2 = 只需要获得地址, 3 = 全都要
     * @return
     */
    public static List<String> getPreviousUpdateList(Integer method) {
        List<String> getPreviousUpdateList = new ArrayList<>();

        Map<String, String> readFileMap = FileIOUtils.readConfigFileToMap(ConstantFromFile.getLogFilePath(), ConstantFromFile.getLogFileName());
        for (Map.Entry<String, String> entryMap : readFileMap.entrySet()) {
            if (method == 1) {
                getPreviousUpdateList.add(entryMap.getKey());
            }
            if (method == 2) {
                getPreviousUpdateList.add(entryMap.getValue());
            }
            if (method == 3) {
                getPreviousUpdateList.add(entryMap.getKey());
                getPreviousUpdateList.add(entryMap.getValue());
            }
        }

        return getPreviousUpdateList;
    }

    /**
     * 写日志
     * @param method    0 追加, 1 覆盖
     * @param text      内容
     * @return          -1 失败, 0 新建成功, 11 已存在并追加, 12 已存在并覆盖
     */
    public static Integer setLog(Integer method, String text) {
        return FileIOUtils.createFile(ConstantFromFile.getLogFilePath(), ConstantFromFile.getLogFileName(), method, text);
    }

}
