package com.njustc.onlinebiz.user.pojo;

public class Tester {
    private int testerId;
    private String testerName;
    private String testerPassword;

    @Override
    public String toString() {
        return "Tester{" +
                "testerId=" + testerId +
                ", testerName='" + testerName + '\'' +
                ", testerPassword='" + testerPassword + '\'' +
                '}';
    }

    public int getTesterId() {
        return testerId;
    }

    public void setTesterId(int testerId) {
        this.testerId = testerId;
    }

    public String getTesterName() {
        return testerName;
    }

    public void setTesterName(String testerName) {
        this.testerName = testerName;
    }

    public String getTesterPassword() {
        return testerPassword;
    }

    public void setTesterPassword(String testerPassword) {
        this.testerPassword = testerPassword;
    }

    public Tester() {
    }

    public Tester(int testerId, String testerName, String testerPassword) {
        this.testerId = testerId;
        this.testerName = testerName;
        this.testerPassword = testerPassword;
    }
}
