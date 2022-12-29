package com.example.a09_objectserialization;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;

public class Person implements Serializable {

    private String name = "";
    private String phone = "";
    private String email = "";
    private static final long serialVersionUID = 1L;

    public Person(String name, String phone, String email) {
        this.name = name;
        this.phone = phone;
        this.email = email;
    }//Person

    //set / get operations
    public String getName() { return name; }//getName
    public void setName(String name) { this.name = name; }//setName

    public String getPhone() { return phone; }//getPhone
    public void setPhone(String phone) { this.phone = phone; }//setPhone

    public String getEmail() { return email; }//getEmail
    public void setEmail(String email) { this.email = email; }//setEmail

    //toString method
    public String toString() {
        return name + ":" + phone + ":" + email;
    }//toString

    public String set_name( String name ) {
        return this.name = name;
    }//set_name

    public String set_phone( String phone ) {
        return this.phone = phone;
    }//set_phone

    public String set_email( String email ) {
        return this.email = email;
    }//set_email
}//Person class
