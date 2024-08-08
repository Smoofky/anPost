/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.lab9.model;

import java.io.Serializable;

public class JwtRequest implements Serializable {
    private static final long serialVersionUID = 5926468583005150707L;
    private String username;
    private String password;
    private String imageUrl;
    // domyślny konstruktor dla parsowania JSON
    public JwtRequest() {
    }
    public JwtRequest(String username, String password, String imageUrl) {
    this.setUsername(username);
    this.setPassword(password);
    this.setImageUrl(imageUrl);
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    
    public String getUsername() {
    return this.username;
    }
    public void setUsername(String username) {
    this.username = username;
    }
    public String getPassword() {
    return this.password;
    }
    public void setPassword(String password) {
    this.password = password;
    }
}
