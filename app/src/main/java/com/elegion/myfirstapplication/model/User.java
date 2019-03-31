package com.elegion.myfirstapplication.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class User implements Serializable {
    @SerializedName("email")
    private String mEmail;
    @SerializedName("name")
    private String mName;
    @SerializedName("password")
    private String mPassword;

    private boolean mHasSuccessEmail;


    public User(String email, String password) {
        mEmail = email;
        mPassword = password;
    }

    public User(String email, String name, String password) {
        mEmail = email;
        mName = name;
        mPassword = password;
    }

    @SerializedName("data")
    @Expose
    private DataBean data;
    private final static long serialVersionUID = -4635470215552223363L;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    //---добавляем внутрений класс DataBean обертку для data
    public static class DataBean {
        @SerializedName("id")
        @Expose
        private Integer mId;
        @SerializedName("name")
        @Expose
        private String mName;
        @SerializedName("email")
        @Expose
        private String mEmail;
        private final static long serialVersionUID = -8198786674952514731L;

        public Integer getId() {
            return mId;
        }

        public void setId(Integer id) {
            this.mId = id;
        }

        public String getName() {
            return mName;
        }

        public void setName(String name) {
            this.mName = name;
        }

        public String getEmail() {
            return mEmail;
        }

        public void setEmail(String email) {
            this.mEmail = email;
        }

    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    public boolean hasSuccessEmail() {
        return mHasSuccessEmail;
    }

    public void setHasSuccessEmail(boolean hasSuccessEmail) {
        mHasSuccessEmail = hasSuccessEmail;
    }

}


