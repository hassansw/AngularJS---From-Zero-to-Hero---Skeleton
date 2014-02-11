package io.devcamp.blog.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 * @author Daniel Sachse
 * @date 08.01.14 19:51
 */

@NamedQueries({
        @NamedQuery(name = User.findUserByEmail, query = "SELECT u FROM User u WHERE u.email = :email")
})
@Entity
public class User extends AbstractEntity {
    public static final String findUserByEmail = "findUserByEmail";

    private String firstname;
    private String lastname;

    @Column(unique = true)
    private String email;
    private String password;

    protected User() {
    }

    public User(String firstname, String lastname, String email, String password) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
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
}