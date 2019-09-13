package br.com.stralom.compras.entities;

import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class User {
    private String uid;
    private String shareCode;
    private String email;
    private String phone;
    private String displayName;

    public User(FirebaseUser firebaseUser) {
        this.uid = firebaseUser.getUid();
        this.shareCode = getRandomShareCode();
        this.email = firebaseUser.getEmail();
        this.phone = firebaseUser.getPhoneNumber();
        this.displayName = firebaseUser.getDisplayName();
    }

    public User(String uid, String shareCode, String email, String phone, String displayName) {
        this.uid = uid;
        this.shareCode = shareCode;
        this.email = email;
        this.phone = phone;
        this.displayName = displayName;
    }

    public Map<String,Object> toJson(){
        Map<String, Object> user = new HashMap<>();
        user.put("uid", this.uid);
        user.put("shareCode", this.shareCode);
        user.put("email", this.email );
        user.put("phone", phone );
        user.put("displayName", displayName );
        return user;
    }

    protected String getRandomShareCode() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 6) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }

    @Override
    public String toString() {
        return "User{" +
                "uid='" + uid + '\'' +
                ", shareCode='" + shareCode + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", displayName='" + displayName + '\'' +
                '}';
    }
}
