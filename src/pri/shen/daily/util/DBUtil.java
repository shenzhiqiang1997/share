package pri.shen.daily.util;

import pri.shen.daily.annotation.Column;
import pri.shen.daily.annotation.Table;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.*;

public class DBUtil {
    //数据库连接参数
    private static final String URL="jdbc:mysql://localhost:3306/";
    private static final String DRIVER="com.mysql.jdbc.Driver";
    private static final String USER="root";
    private static final String PASSWORD="1234";

    private Connection connection;
    private PreparedStatement ps;
    private ResultSet rs;

    //加载数据库驱动
    static {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    //根据传入的数据库获取相应的连接
    public DBUtil(String databaseName){
        try {
            connection=DriverManager.getConnection(URL+databaseName,USER,PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public <T> List<T> query(T entity){
        //获取类类型 其中包括该实体类的信息 包括属性 方法 构造函数
        Class entityClass=entity.getClass();

        //用于盛放搜索结果
        List<T> results=new ArrayList<>();

        //如果用Table标记过则表明映射为数据库表
        if (entityClass.isAnnotationPresent(Table.class)){
            //获取注释
            Table tableAnnotation= (Table) entityClass.getAnnotation(Table.class);

            //获取注释value即对应表名
            String tableName=tableAnnotation.value();

            //拼接sql语句 SELECT * FROM tableName WHERE 1=1
            StringBuffer sql=new StringBuffer("SELECT * FROM ").append(tableName).append(" WHERE 1=1");

            //用于放搜索条件的顺序和搜索值
            Map<Integer,Object> searchColumnParams=new HashMap<>();
            Integer paramIndex=1;

            //用于存放搜索结果的顺序和用于绑定返回数据的setter方法名以及参数类型
            List<Map<String,Object>> resultColumnParams=new ArrayList<>();

            //获取该实体类的所有自己声明的属性
            Field[] fields=entityClass.getDeclaredFields();
            for (Field field:fields) {
                //如果用Column标记过则说明要映射为表中的一列
                if (field.isAnnotationPresent(Column.class)){
                    //存放映射的每列的setter方法和参数类型
                    Map<String,Object> resultColumnMap=new HashMap<>();
                    resultColumnMap.put("setter","set"+field.getName().substring(0,1).toUpperCase()+field.getName().substring(1));
                    resultColumnMap.put("paramClass",field.getType());
                    resultColumnParams.add(resultColumnMap);

                    //获取到要搜索的列名
                    Column columnAnnotation=field.getAnnotation(Column.class);
                    String columnName=columnAnnotation.value();
                    String fieldName=field.getName();


                    try {
                        //调用相应的getter 获取到搜索值
                        Method method=entityClass.getDeclaredMethod("get"+fieldName.substring(0,1).toUpperCase()+fieldName.substring(1));
                        Object searchColumnValue=method.invoke(entity);

                        //如果获得到搜索值说明要该列有搜索条件
                        if (searchColumnValue!=null){
                            //拼接相应的SQL语句 AND 列名=/LIKE ?
                            if (searchColumnValue instanceof String){
                                sql.append(" AND "+columnName+" LIKE ?");
                                searchColumnValue="%"+searchColumnValue+"%";
                            }else {
                                sql.append(" AND "+columnName+" = ?");
                            }
                            //将搜索条件按顺序存放 用于preparedStatement里设置搜索条件
                            searchColumnParams.put(paramIndex++,searchColumnValue);
                        }
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }


            try {

                //根据拼接好的sql语句获取到prepareStatement
                ps=connection.prepareStatement(sql.toString());
                Set<Map.Entry<Integer,Object>> entrySet=searchColumnParams.entrySet();

                //将之前存放的搜索条件依次设置到prepareStatement里
                for (Map.Entry<Integer,Object> entry: entrySet) {
                    ps.setObject(entry.getKey(),entry.getValue());
                }

                //执行搜索
                rs=ps.executeQuery();

                //遍历结果集
                while (rs.next()){
                    try {
                        //获取到对应实体类的构造函数
                        Constructor entityConstructor=entityClass.getDeclaredConstructor();
                        try {
                            //调用构造函数获取到实例
                            T resultRow=(T)entityConstructor.newInstance();

                            //根据之前存放的setter信息调用创建的实例的各个setter 绑定返回值
                            for (int i=0;i<resultColumnParams.size();i++){
                                Map<String,Object> resultMap=resultColumnParams.get(i);
                                Method setter=entityClass.getDeclaredMethod((String)resultMap.get("setter"),(Class)resultMap.get("paramClass"));
                                setter.invoke(resultRow,rs.getObject(i+1));
                            }

                            //将得到的每行对应的实例放到结果里
                            results.add(resultRow);
                        } catch (InstantiationException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if (rs!=null){
                    try {
                        rs.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                if (ps!=null){
                    try {
                        ps.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

            }


        }
        //将搜索结果返回
        return results;
    }
}
