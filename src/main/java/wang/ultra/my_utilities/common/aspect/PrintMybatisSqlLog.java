package wang.ultra.my_utilities.common.aspect;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.util.*;

@Component
@Aspect
public class PrintMybatisSqlLog {

    private static final Logger LOG = LoggerFactory.getLogger(PrintMybatisSqlLog.class);

    @Pointcut("execution(* wang.ultra.my_utilities.common.*.mapper.*.*(..))")
    private void commonPointcut() {

    }

    @Pointcut("execution(* wang.ultra.my_utilities.*.mapper.*.*(..))")
    private void anyonePointcut() {

    }

    @Autowired
    private List<SqlSessionFactory> sqlSessionFactoryList;

    @Around("commonPointcut() || anyonePointcut()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object proceed = proceedingJoinPoint.proceed();

        String sql = getMyBatisSql(proceedingJoinPoint, sqlSessionFactoryList);
        System.out.println("解析SQL语句: " + sql);

        return proceed;
    }

    public static String getMyBatisSql(ProceedingJoinPoint proceedingJoinPoint, List<SqlSessionFactory> sqlSessionFactoryList) throws IllegalAccessException {
        Map<String, Object> map = new HashMap<>();

        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        Method method = signature.getMethod();

        Object[] objects = proceedingJoinPoint.getArgs();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        for (int i = 0; i < parameterAnnotations.length; i++) {
            Object o = objects[i];
            if (parameterAnnotations[i].length == 0) {
                if (o.getClass().getClassLoader() == null && o instanceof Map) {
                    map.putAll((Map<? extends String, ?>) o);
                } else {
                    map.putAll(objectToMap(o));
                }
//                map.putAll(objectToMap(o));
            } else {
                for (Annotation annotation : parameterAnnotations[i]) {
                    if (annotation instanceof Param) {
                        map.put(((Param) annotation).value(), o);
                    }
                }
            }
        }

        String name = method.getDeclaringClass().getName();
        String methodName = method.getName();
        String result = null;
        for (SqlSessionFactory ssf : sqlSessionFactoryList) {
            Configuration configuration = ssf.getConfiguration();
            Collection<String> mappedStatementNames = configuration.getMappedStatementNames();
            if (mappedStatementNames.contains(name + "." + methodName)) {
                MappedStatement mappedStatement = configuration.getMappedStatement(name + "." + methodName);
                BoundSql boundSql = mappedStatement.getBoundSql(map);
                result = generateSql(configuration, boundSql);
            }
        }
        return result;
    }

    /**
     * 把mybatis里面的sql解析成不带问号占位符的sql
     *
     * @param configuration
     * @param boundSql
     * @return
     */
    private static String generateSql(Configuration configuration, BoundSql boundSql) {
        Object parameterObject = boundSql.getParameterObject();
        List<ParameterMapping> parameterMappingList = boundSql.getParameterMappings();
        String sql = boundSql.getSql().replaceAll("[\\s]+", " ");
        if (!parameterMappingList.isEmpty() && parameterObject != null) {
            TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
            if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                sql = sql.replaceFirst("\\?", generateQuestionMarkInStringOrDate(parameterObject));
            } else {
                MetaObject metaObject = configuration.newMetaObject(parameterObject);
                for (ParameterMapping parameterMapping : parameterMappingList) {
                    String propertyName = parameterMapping.getProperty();
                    String[] strArr = metaObject.getObjectWrapper().getGetterNames();
                    Arrays.toString(strArr);
                    if (metaObject.hasGetter(propertyName)) {
                        Object o = metaObject.getValue(propertyName);
                        sql = sql.replaceFirst("\\?", generateQuestionMarkInStringOrDate(o));
                    } else if (boundSql.hasAdditionalParameter(propertyName)) {
                        Object o = boundSql.getAdditionalParameter(propertyName);
                        sql = sql.replaceFirst("\\?", generateQuestionMarkInStringOrDate(o));
                    }
                }
            }
        }
        return sql;
    }

    /**
     * 给String和Date参数两边加上单引号
     *
     * @param o
     * @return
     */
    private static String generateQuestionMarkInStringOrDate(Object o) {
        String value = null;
        if (o instanceof String) {
            value = "'" + o.toString() + "'";
        } else if (o instanceof Date) {
            DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.CHINA);
            value = "'" + dateFormat.format(new Date()) + "'";
        } else {
            if (o != null) {
                value = o.toString();
            } else {
                value = "";
            }
        }

        return value;
    }

    private static Map<String, Object> objectToMap(Object o) throws IllegalAccessException {
        Map<String, Object> map = new HashMap<>();
        Class<?> clazz = o.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            ReflectionUtils.makeAccessible(field);
            String fieldName = field.getName();
            Object value = field.get(o);
            map.put(fieldName, value);
        }
        return map;
    }

}
