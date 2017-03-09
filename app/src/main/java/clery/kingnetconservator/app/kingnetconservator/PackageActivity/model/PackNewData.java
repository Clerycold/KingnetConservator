package clery.kingnetconservator.app.kingnetconservator.PackageActivity.model;

import java.util.List;

/**
 * Created by clery on 2017/2/18.
 */

public class PackNewData {

    private List<String> create_date;//包裹創建日期
    private List<String> postal_type;//包裹類型
    private List<String> p_name;//收件人 名稱
    private List<String> postal_logistics;//例如：大榮貨運
    private List<String> postal_id;//包裹ID
    private List<String>is_private;//是否為私人
    private List<String>transport_code;//包裹號
    private List<String>p_note;//備註

    private List<String>pictureUrl;

    public List<String> getCreate_date() {
        return create_date;
    }

    public void setCreate_date(List<String> create_date) {
        this.create_date = create_date;
    }

    public List<String> getPostal_logistics() {
        return postal_logistics;
    }

    public void setPostal_logistics(List<String> postal_logistics) {
        this.postal_logistics = postal_logistics;
    }

    public List<String> getIs_private() {
        return is_private;
    }

    public void setIs_private(List<String> is_private) {
        this.is_private = is_private;
    }

    public List<String> getTransport_code() {
        return transport_code;
    }

    public void setTransport_code(List<String> transport_code) {
        this.transport_code = transport_code;
    }

    public List<String> getP_note() {
        return p_note;
    }

    public void setP_note(List<String> p_note) {
        this.p_note = p_note;
    }

    public List<String> getPostal_type() {
        return postal_type;
    }

    public void setPostal_type(List<String> postal_type) {
        this.postal_type = postal_type;
    }

    public List<String> getP_name() {
        return p_name;
    }

    public void setP_name(List<String> p_name) {
        this.p_name = p_name;
    }

    public List<String> getPostal_id() {
        return postal_id;
    }

    public void setPostal_id(List<String> postal_id) {
        this.postal_id = postal_id;
    }

    public List<String> getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(List<String> pictureUrl) {
        this.pictureUrl = pictureUrl;
    }
}
