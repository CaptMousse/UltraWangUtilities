package wang.ultra.my_utilities.common.config.interceptor;

import org.apache.commons.lang3.time.StopWatch;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Properties;

@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
public class MybatisFinalSqlIntercepter implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();

        System.out.println("歪比巴卜");

        StopWatch stopWatch = StopWatch.createStarted();

        if (invocation.getTarget() instanceof StatementHandler statementHandler) {

            Connection connection = (Connection) invocation.getArgs()[0];
            Statement statement = statementHandler.prepare(connection, 30);
            statementHandler.parameterize(statement);

            MetaObject metaObject = SystemMetaObject.forObject(statement);

            while (metaObject.hasGetter("h")) {
                Object o = metaObject.getValue("h");
                metaObject = SystemMetaObject.forObject(o);
            }

            Field metaField = metaObject.getClass().getDeclaredField("originalObject");
            ReflectionUtils.makeAccessible(metaField);
            Object object = metaField.get(metaObject);
            Field statementField = metaField.getClass().getDeclaredField("statement");
            ReflectionUtils.makeAccessible(statementField);
            Object statementObject = statementField.get(object);
            Field stmtField = statementObject.getClass().getDeclaredField("stmt");
            ReflectionUtils.makeAccessible(stmtField);
            Object stmtObject = stmtField.get(statementObject);
            Field statementArrivedField = stmtObject.getClass().getDeclaredField("statement");
            ReflectionUtils.makeAccessible(statementArrivedField);
            Object statementArrivedObject = statementArrivedField.get(stmtObject);

            String finalSql = statementArrivedObject.toString();

            finalSql = finalSql.substring(finalSql.lastIndexOf(":") + 1, finalSql.length() - 1);

            System.out.println("最终sql : " + finalSql);
        }

        System.out.println("耗时: " + stopWatch);


        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Interceptor.super.plugin(target);
    }

    @Override
    public void setProperties(Properties properties) {
        Interceptor.super.setProperties(properties);
    }
}
