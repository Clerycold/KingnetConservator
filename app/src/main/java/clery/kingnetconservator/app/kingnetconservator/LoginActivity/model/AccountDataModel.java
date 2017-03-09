package clery.kingnetconservator.app.kingnetconservator.LoginActivity.model;

import android.content.Context;

import clery.kingnetconservator.app.kingnetconservator.Control.SaveData;

/**
 * Created by clery on 2017/2/21.
 */

public class AccountDataModel {

    private String account;
    private String password;
    private String opencode;
    private boolean isCheckRemember;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOpencode() {
        return opencode;
    }

    public void setOpencode(String opencode) {
        this.opencode = opencode;
    }

    public boolean isCheckRemember() {
        return isCheckRemember;
    }

    public void setCheckRemember(boolean checkRemember) {
        isCheckRemember = checkRemember;
    }
}
