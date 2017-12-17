package pri.shen.daily.test;

import org.junit.Test;
import pri.shen.daily.entity.Student;
import pri.shen.daily.entity.Teacher;
import pri.shen.daily.util.DBUtil;

import java.io.*;
import java.util.List;
import java.util.Random;

public class TestDBUtil {
    private DBUtil dbUtil;

    public TestDBUtil(){
        dbUtil=new DBUtil("daily");
    }
    @Test
    public void testQueryStudent(){
        Student student=new Student();
        student.setStudentId("201622010100");
        List<Student> students=dbUtil.query(student);
        System.out.println(students);

    }
    @Test
    public void testQueryTeacher(){
        Teacher teacher=new Teacher();
        teacher.setDuty("辅导员");
        List<Teacher> teachers=dbUtil.query(teacher);
        System.out.println(teachers);
    }
    @Test
    public void generateTestData(){
        String sql="INSERT student VALUES ";
        Long studentId=2016220101001L;
        String name="测试姓名";
        String major="测试专业";
        String contactWay="测试联系方式";
        Random random=new Random();
        FileOutputStream fileOutputStream=null;
        BufferedWriter bufferedWriter=null;
        try {
            fileOutputStream=new FileOutputStream("test.sql");
            bufferedWriter=new BufferedWriter(new OutputStreamWriter(fileOutputStream));
            for (int i=0;i<50;i++){
                String insertSql=sql;
                insertSql+="('"+(studentId+i)+"','"+name+i+"',"+random.nextInt(2)+",'"+major+i+"','"+contactWay+i+"');";
                bufferedWriter.write(insertSql);
                bufferedWriter.newLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try{
                if (bufferedWriter!=null){
                    bufferedWriter.close();
                }
                if (fileOutputStream!=null){
                    fileOutputStream.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }
}
