package com.example.sharecare.models;

public class Kid {

    private int age;
    private String name;
    private String gender;
    private Parent parent;
    private String schoolName;


    public Kid(int age, String name, String gender, Parent parent, String schoolName) {
        this.age = age;
        this.name = name;
        this.gender = gender;
        this.parent = parent;
        this.schoolName = schoolName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Parent getParent() {
        return parent;
    }

    public void setParent(Parent parent) {
        this.parent = parent;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    @Override
    public String toString() {
        return "Kid{" +
                "age=" + age +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", parent=" + parent +
                ", schoolName='" + schoolName + '\'' +
                '}';
    }
}

