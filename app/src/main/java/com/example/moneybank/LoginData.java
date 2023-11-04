package com.example.moneybank;

public class LoginData {
    private String login;
    private String password;

    // Constructeur par défaut sans paramètres (requis par Firebase)
    public LoginData() {
        // N'ajoutez pas de code ici, laissez-le vide
    }

    public LoginData(String login, String password) {
        this.login = login;
        this.password = password;
    }

    // Getter pour le login
    public String getLogin() {
        return login;
    }

    // Setter pour le login
    public void setLogin(String login) {
        this.login = login;
    }

    // Getter pour le mot de passe
    public String getPassword() {
        return password;
    }

    // Setter pour le mot de passe
    public void setPassword(String password) {
        this.password = password;
    }
}
