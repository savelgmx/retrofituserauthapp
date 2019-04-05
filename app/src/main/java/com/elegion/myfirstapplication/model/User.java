package com.elegion.myfirstapplication.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class User {

    @Expose
    @SerializedName("data")
    private final UserBean mData;

    public User(UserBean data) {
        mData = data;
    }

    public UserBean getData() {
        return mData;
    }

    public static class UserBean implements Serializable {

        @SerializedName("email")
        private String login;
        @Expose
        @SerializedName("name")
        private String name;

        @Expose
        @SerializedName("password")
        private String password;

        public UserBean(String login, String name, String password) {
            this.login = login;
            this.name = name;
            this.password = password;
        }

        private Boolean mHasSuccessLogin = false;

        private Boolean hasSuccessLogin() {
            return mHasSuccessLogin;
        }

        private void setHasSuccessLogin(Boolean hasSuccessLogin) {
            mHasSuccessLogin = hasSuccessLogin;
        }


        public String getEmail() {
            return login;
        }

        public String getName() {
            return name;
        }
    }
}



