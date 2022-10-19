package globalServer;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Random;

/**@author Ammar Darwesh, Malek Abdul Sater  @coauthor**/
public class User implements Serializable {

    private String firstName, surName, street, zipCode, city, userName, password, email;

    public User(String firstName, String surName, String street, String zipCode, String city, String email) {


        this.firstName = firstName;
        this.surName = surName;
        this.street = street;
        this.zipCode = zipCode;
        this.city = city;
        this.email = email;
        password = "";
        userName = "";

    }

    public User() {
        firstName = "";
        surName = "";
        street = "";
        zipCode = "";
        city = "";
        password = "";
        userName = "";
        email = "";
    }

    public User(String firstName, String surName, String street) {
        this.firstName = firstName;
        this.surName = surName;
        this.street = street;
        zipCode = "";
        city = "";
        email = "";
        password = "";
        userName = "";
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void generateLogInDetails() {
        Random rand = new Random();
        String characters = "123456789abcdefghijklmnopqrstuvwxyz";
        for (int i = 0; i < 8; i++) {
            password += characters.charAt(rand.nextInt(characters.length()));
        }

        String firstName = getFirstName().toLowerCase().replaceAll("\\s", "");
        String surName = getSurName().toLowerCase().replaceAll("\\s", "");
        String street = getStreet().toLowerCase().replaceAll("\\s", "");
        userName = street.substring(0, 3) + surName.substring(0, 2) + firstName.substring(0, 2);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String[] getUserInfo(){

        String[] userInfo = new String[8];

        userInfo[0] = firstName;
        userInfo[1] = surName;
        userInfo[2] = street;
        userInfo[3] = zipCode;
        userInfo[4] = city;
        userInfo[5] = userName;
        userInfo[6] = password;
        userInfo[7] = email;

        return userInfo;
    }

    public String getFormattedFirstName () {
        return getFirstName().substring(0,1).toUpperCase() + getFirstName().substring(1);
    }

    public void generateRandomUserName(){

        Random rand = new Random();
        int random = rand.nextInt(4)+1;
        int randomNumber =rand.nextInt(9999);

        userName = street.substring(0, random) + surName.substring(0,random) + firstName.substring(0,random) + randomNumber;
    }
}
