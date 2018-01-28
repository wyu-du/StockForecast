package GetStockInfo.Sina;

import java.util.HashMap;
import org.apache.commons.dbcp.BasicDataSource;

import org.springframework.jdbc.core.JdbcTemplate;

/**
 *
 * @author hu
 */
public class JDBCHelper {

    public static HashMap<String, JdbcTemplate> templateMap 
        = new HashMap<String, JdbcTemplate>();

    public static JdbcTemplate createMysqlTemplate(String templateName, 
            String url, String username, String password, 
            int initialSize, int maxActive) {

        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setInitialSize(initialSize);
        dataSource.setMaxActive(maxActive);
        JdbcTemplate template = new JdbcTemplate(dataSource);
        templateMap.put(templateName, template);
        return template;
    }

    public static JdbcTemplate getJdbcTemplate(String templateName){
        return templateMap.get(templateName);
    }

}