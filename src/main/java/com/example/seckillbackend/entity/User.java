package com.example.seckillbackend.entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "t_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    private String salt;

    private String nickname;

    private String email;

    private String mobile;

    @Temporal(TemporalType.TIMESTAMP)
    private Date registerDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastLoginDate;

    private Integer loginCount = 0;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id;}
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getSalt() { return salt; }
    public void setSalt(String salt) { this.salt = salt; }
    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getMobile() { return mobile; }
    public void setMobile(String mobile) { this.mobile = mobile; }
    public Date getRegisterDate() { return registerDate; }
    public void setRegisterDate(Date registerDate) { this.registerDate = registerDate; }
    public Date getLastLoginDate() { return lastLoginDate; }
    public void setLastLoginDate(Date lastLoginDate) { this.lastLoginDate = lastLoginDate; }
    public Integer getLoginCount() { return loginCount; }
    public void setLoginCount(Integer loginCount) { this.loginCount = loginCount; }
}