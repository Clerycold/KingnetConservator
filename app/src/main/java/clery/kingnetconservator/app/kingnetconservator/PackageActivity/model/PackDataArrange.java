package clery.kingnetconservator.app.kingnetconservator.PackageActivity.model;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import clery.kingnetconservator.app.kingnetconservator.PackageActivity.model.PackData;
import clery.kingnetconservator.app.kingnetconservator.PackageActivity.model.PackNewData;

/**
 * Created by clery on 2017/2/17.
 */

public class PackDataArrange {

    private static List<PackNewData> packNewDataList;

    private static String[] TABLET_NOTE_OK;

    //取得所有用戶資料 進行用戶資料比對 相同去除
    // 利用 Set 的特性，將所有項目放入 Set中即可移除重複的項目
    public static String[] nonDuplicate(List<PackData> packDataList){

        String[] tempOld = new String[packDataList.size()];

        for(int i =0,j = packDataList.size(); i < j ;i++){
            tempOld[i] = packDataList.get(i).getTablet_note();
        }

        //HashSet 不重複 順序不一樣
        //LinkedHashSet 不重複 順序一樣
        Set<String> intSet = new LinkedHashSet<String>();
        for (String element : tempOld) {
            intSet.add(element);
            }

        String nonDuplicate[] = new String[intSet.size()];

        Object[] tempArray = intSet.toArray();
        for (int i = 0; i < tempArray.length; i++) {
            nonDuplicate[i] = (String) tempArray[i];
            }
        return nonDuplicate;
    }

    public static List<PackNewData> ComparisonPackData(List<PackData> packDataList,String[] nonDuplicate_ok){
        String[] nonDuplicate ;
        if(nonDuplicate_ok == null){
            nonDuplicate = nonDuplicate(packDataList);
        }else{
            nonDuplicate = nonDuplicate_ok;
        }
        List<PackNewData> packNewDataList = new ArrayList<>();
        PackNewData packNewData;
        for(int i=0 ,j=nonDuplicate.length;i<j;i++){
            packNewData = new PackNewData();
            String tablet_note = nonDuplicate[i];

            List<String> postal_type = new ArrayList();
            List<String> p_name = new ArrayList();
            List<String> create_date = new ArrayList<>();
            List<String> postal_logistics = new ArrayList<>();
            List<String> postal_id = new ArrayList<>();
            List<String> is_private = new ArrayList<>();
            List<String> transport_code = new ArrayList<>();
            List<String> p_note = new ArrayList<>();
            List<String> pictureurl = new ArrayList<>();

            for(int x=0,y=packDataList.size();x<y;x++){

                if(tablet_note.equals(packDataList.get(x).getTablet_note())){
                    postal_type.add(packDataList.get(x).getPostal_type());
                    p_name.add(packDataList.get(x).getP_name());
                    create_date.add(packDataList.get(x).getCreate_date());
                    postal_logistics.add(packDataList.get(x).getPostal_logistics());
                    postal_id.add(packDataList.get(x).getPostal_id());
                    is_private.add(packDataList.get(x).getIs_private());
                    transport_code.add(packDataList.get(x).getTransport_code());
                    p_note.add(packDataList.get(x).getP_note());
                    pictureurl.add(packDataList.get(x).getPicture_Url());
                }
            }

            packNewData.setPostal_type(postal_type);
            packNewData.setP_name(p_name);
            packNewData.setCreate_date(create_date);
            packNewData.setPostal_logistics(postal_logistics);
            packNewData.setPostal_id(postal_id);
            packNewData.setIs_private(is_private);
            packNewData.setTransport_code(transport_code);
            packNewData.setP_note(p_note);
            packNewData.setPictureUrl(pictureurl);

            packNewDataList.add(packNewData);
        }
        return packNewDataList;
    }

    public static String[] getTabletNoteOk() {
        return TABLET_NOTE_OK;
    }

    public static List<PackNewData> getPackNewDataList() {
        return packNewDataList;
    }

    public static String getTableNotePosition(int position){
        return TABLET_NOTE_OK[position];
    }

    public static void resetListdata(List<PackData> packDataList){
        TABLET_NOTE_OK = nonDuplicate(packDataList);
        packNewDataList =ComparisonPackData(packDataList,TABLET_NOTE_OK);

    }
    public static void clearArrangeData(){
        TABLET_NOTE_OK = null;
        packNewDataList = null;
    }
}
