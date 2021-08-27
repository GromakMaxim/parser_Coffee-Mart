package org.gromak;

public enum Options {
    DBURL("jdbc:mysql://localhost:3306/my_db?useSSL=false&amp&serverTimezone=UTC"),
    DBPASSWORD("bestuser"),
    DBUSER("bestuser");




    String value;
    Options() {
    }
    Options(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
