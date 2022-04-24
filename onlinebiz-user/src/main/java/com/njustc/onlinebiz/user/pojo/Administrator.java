package com.njustc.onlinebiz.user.pojo;

public class Administrator {
    private int administratorId;
    private String administratorName;
    private String administratorPassword;

    @Override
    public String toString() {
        return "Administrator{" +
                "administratorId=" + administratorId +
                ", administratorName='" + administratorName + '\'' +
                ", administratorPassword='" + administratorPassword + '\'' +
                '}';
    }

    public int getAdministratorId() {
        return administratorId;
    }

    public void setAdministratorId(int administratorId) {
        this.administratorId = administratorId;
    }

    public String getAdministratorName() {
        return administratorName;
    }

    public void setAdministratorName(String administratorName) {
        this.administratorName = administratorName;
    }

    public String getAdministratorPassword() {
        return administratorPassword;
    }

    public void setAdministratorPassword(String administratorPassword) {
        this.administratorPassword = administratorPassword;
    }

    public Administrator() {
    }

    public Administrator(int administratorId, String administratorName, String administratorPassword) {
        this.administratorId = administratorId;
        this.administratorName = administratorName;
        this.administratorPassword = administratorPassword;
    }
}
