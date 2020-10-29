package filewebapp.entities;

import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name="users")
public class User {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "login", unique = true)
    private String login;
    @Column(name = "email", unique = true)
    private String email;
    @Column(name = "password_hash")
    private String passwordHash;
    @Column(name = "date_created")
    private Date dateCreated;
    @Column(name = "home_catalog", unique = true)
    private String homeCatalog;


    public User(){

    }

    public User(String login, String email, String passwordHash) {
        this.login = login;
        this.email = email;
        this.passwordHash = passwordHash;
        this.dateCreated = new Date();
        this.homeCatalog = login;
    }

    public String getLogin() {
        return login;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }
}