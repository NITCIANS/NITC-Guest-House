package com.example.guesthousebooking;

public class User
{

    private String name;
    private String email;
    private String contact;
    private String type;
    private String accountStatus;

    public String getAccountStatus(){
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    public User()
    {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public String toString()
    {
        return String.format("Name : " + name + "\nAccount Status : " + accountStatus);
    }
}
