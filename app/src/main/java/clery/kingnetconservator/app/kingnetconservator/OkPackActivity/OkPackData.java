package clery.kingnetconservator.app.kingnetconservator.OkPackActivity;

import java.util.List;

import clery.kingnetconservator.app.kingnetconservator.PackageActivity.model.PackNewData;

/**
 * Created by clery on 2017/2/24.
 */

public class OkPackData {

    private List<PackNewData> packNewDatalist;
    private String[] TabletNote;

    public String[] getTabletNote() {
        return TabletNote;
    }

    public void setTabletNote(String[] tabletNote) {

        TabletNote = tabletNote;
    }

    public List<PackNewData> getPackNewDatalist() {
        return packNewDatalist;
    }

    public void setPackNewDatalist(List<PackNewData> packNewDatalist) {
        this.packNewDatalist = packNewDatalist;
    }
}
