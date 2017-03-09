package clery.kingnetconservator.app.kingnetconservator.PackageActivity.model;

import android.content.Context;
import android.support.annotation.Nullable;

import java.util.List;

import clery.kingnetconservator.app.kingnetconservator.Control.RemindToast;

/**
 * Created by clery on 2017/2/17.
 */

public class PackData {

    //資料創造日期
    String create_date;
    //是否為私人包裹
    String is_private;
    //收件人姓名
    String p_name;
    //包裹類型
    String postal_type;
    //收件人地址
    String tablet_note;
    //資料戶
    String serial_num;
    //例如：大榮貨運
    String postal_logistics;
    //包裹號
    String transport_code;
    //備註
    String p_note;
    //包裹id
    String postal_id;

    //圖片連結
    String picture_Url;


    public String getPostal_logistics() {
        return postal_logistics;
    }

    public void setPostal_logistics(String postal_logistics) {
        this.postal_logistics = postal_logistics;
    }

    public String getTransport_code() {
        return transport_code;
    }

    public void setTransport_code(String transport_code) {
        this.transport_code = transport_code;
    }

    public String getP_note() {
        return p_note;
    }

    public void setP_note(String p_note) {
        this.p_note = p_note;
    }

    public String getSerial_num() {
        return serial_num;
    }

    public void setSerial_num(String serial_num) {
        this.serial_num = serial_num;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public String getIs_private() {
        return is_private;
    }

    public void setIs_private(String is_private) {
        this.is_private = is_private;
    }

    public String getP_name() {
        return p_name;
    }

    public void setP_name(String p_name) {
        this.p_name = p_name;
    }

    public String getPostal_type() {
        return postal_type;
    }

    public void setPostal_type(String postal_type) {
        this.postal_type = postal_type;
    }

    public String getTablet_note() {
        return tablet_note;
    }

    public void setTablet_note(String tablet_note) {
        this.tablet_note = tablet_note;
    }

    public String getPostal_id() {
        return postal_id;
    }

    public void setPostal_id(String postal_id) {
        this.postal_id = postal_id;
    }

    public String getPicture_Url() {
        return picture_Url;
    }

    public void setPicture_Url(String picture_Url) {
        this.picture_Url = picture_Url;
    }
}
