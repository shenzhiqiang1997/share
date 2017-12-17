package pri.shen.daily.entity;

import pri.shen.daily.annotation.Column;
import pri.shen.daily.annotation.Table;

@Table("teacher")
public class Teacher {
    @Column("teacher_id")
    private String teacherId;
    @Column("name")
    private String name;
    @Column("duty")
    private String duty;
    @Column("email")
    private String email;

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDuty() {
        return duty;
    }

    public void setDuty(String duty) {
        this.duty = duty;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "teacherId='" + teacherId + '\'' +
                ", name='" + name + '\'' +
                ", duty='" + duty + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
