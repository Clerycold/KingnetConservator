package clery.kingnetconservator.app.kingnetconservator.Control.Net;

/**
 * Created by clery on 2017/2/18.
 */

public class KingnetServerLocation {

    private static String kingnetService = "http://www.kingnetcare.com.tw:8088/api/Postal/getNonReceivedPostalList";

    public static String getKingnetService(){
        return kingnetService;
    }
}
