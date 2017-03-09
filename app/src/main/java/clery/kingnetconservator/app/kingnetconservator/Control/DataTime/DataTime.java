package clery.kingnetconservator.app.kingnetconservator.Control.DataTime;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by clery on 2017/3/3.
 */

public class DataTime {

    public static String getNowTime(String dateformat) {
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat(dateformat);// 可以方便地修改日期格式
        return dateFormat.format(now);
    }
}
