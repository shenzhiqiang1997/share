package pri.shen.daily.entity;

import pri.shen.daily.annotation.Column;
import pri.shen.daily.annotation.Table;

@Table("student")
public class Student {
    @Column("student_id")
    private String studentId;
    @Column("name")
    private String name;
    @Column("sex")
    private Integer sex;
    @Column("major")
    private String major;
    @Column("contact_way")
    private String contactWay;

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getContactWay() {
        return contactWay;
    }

    public void setContactWay(String contactWay) {
        this.contactWay = contactWay;
    }

    @Override
    public String toString() {
        return "Student{" +
                "studentId=" + studentId +
                ", name='" + name + '\'' +
                ", sex=" + sex +
                ", major='" + major + '\'' +
                ", contactWay='" + contactWay + '\'' +
                '}';
    }
}
