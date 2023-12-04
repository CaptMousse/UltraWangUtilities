package wang.ultra.my_utilities.common.conrtoller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wang.ultra.my_utilities.common.utils.AjaxUtils;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/UltraWangUtilities")
public class BaseController {
    @GetMapping("getVersion")
    public AjaxUtils getVersion() {

        // 版本号
        String path = "META-INF" + File.separator + "maven" + File.separator + "wang.ultra" + File.separator + "UltraWangUtilities" + File.separator + "pom.properties";
        ClassPathResource resource = new ClassPathResource(path);

        String strText;
        Map<String, String> stringMap = new HashMap<>();
        try {
            InputStream inputStream = resource.getInputStream();
            InputStreamReader reader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(reader);
            while ((strText = bufferedReader.readLine()) != null) {
                // System.out.println("strText = " + strText);
                if (!strText.isEmpty()) { // 略过空行
                    String strSubString = strText.substring(0, 1);
                    if (!"#".equals(strSubString)) { // 略过注释符号
                        String[] strArr = strText.split("=");
                        stringMap.put(strArr[0], strArr[1]);
                    }
                }
            }
        } catch (IOException e) {
            return AjaxUtils.success("当前模式不提供版本号支持", "0");
        }
        Object version = stringMap.get("version");
        return AjaxUtils.success(version);
    }

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    /**
     * 测试用的方法
     * @return
     */
    @GetMapping("getUser")
    public AjaxUtils getUser() {
        String sql = "SELECT * FROM stock_data WHERE stock_id = :stockId";
        MapSqlParameterSource stockMSPS = new MapSqlParameterSource();
        stockMSPS.addValue("stockId", "600919");

        List<Map<String, Object>> list = namedParameterJdbcTemplate.queryForList(sql, stockMSPS);
        return AjaxUtils.success(list);
    }
}
