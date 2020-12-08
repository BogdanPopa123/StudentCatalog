package com.packagename.myapp.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.validator.constraints.Email;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "user")
public class User extends BaseModel {

    private static final Logger logger = LogManager.getLogger(User.class);
    private static ObjectMapper mapper;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NotNull(message = "Enter message")
    private String username = "default";

    @NotNull(message = "Enter email")
    @Email(message = "Not an email")
    private String email = "default@default.com";

    @NotNull(message = "Enter password")
    private String password;

    @NotNull(message = "Enter name")
    private String name = "default";

    @NotNull(message = "Enter surname")
    private String surname = "default";

    private String birthDay;

    @NotNull
    private String cnp = "default";

    @NotNull
    private String address = "default";

    @NotNull
    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.STUDENT;

    private boolean isAdmin = false;

    private byte[] image;

    private String phoneNumber;

    private static ObjectMapper getMapper() {
        if (mapper == null) {
            mapper = new ObjectMapper();
        }

        return mapper;
    }

    public static User jsonParse(String jsonString) {
        try {
            logger.trace("Trying to parse JSON to User model");
            return getMapper().readValue(jsonString, User.class);
        } catch (JsonProcessingException e) {
            logger.warn("Error on parsing User FROM JSON: " + jsonString, e);
            return new User();
        }

    }

    @JsonIgnore
    public static User getAnonymousUser() {
        User anonymousUser = new User();
        anonymousUser.setUsername("AnonymousUsername");
        return anonymousUser;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    public String getCnp() {
        return cnp;
    }

    public void setCnp(String cnp) {
        this.cnp = cnp;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    @JsonIgnore
    public String getFullName() {
        return this.name + " " + this.surname;
    }

    @JsonIgnore
    public boolean isNotAdmin() {
        return !isAdmin();
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", birthDay='" + birthDay + '\'' +
                ", role=" + role +
                ", isAdmin=" + isAdmin +
                ", image=" + Arrays.toString(image) +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }

    public String toJSON() {
        try {
            logger.debug("Trying to parse User model to JSON");
            return getMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            logger.warn("Error on parsing User TO JSON: " + this.toString(), e);
            return "";
        }
    }

    public boolean checkAnonymous() {
        return getAnonymousUser().getUsername().equals(this.getUsername());
    }

    @Override
    @JsonIgnore
    public BaseModel getParent() {
        return null;
    }

    @Override
    @JsonIgnore
    public void setParent(BaseModel parent) {

    }

    @Override
    @JsonIgnore
    public List<BaseModel> getChildren() {
        return null;
    }
}


