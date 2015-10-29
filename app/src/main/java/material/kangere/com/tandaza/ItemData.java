package material.kangere.com.tandaza;

/**
 * Created by user on 10/21/2015.
 */
public class ItemData {

    private String title;
    private String nid;
    private String image_path;
    private String time_stamp;


    public ItemData() {


    }
    public ItemData(
            String nid,String title,String image_path,String time_stamp
    ) {

        this.nid = nid;
        this.title = title;
        this.image_path = image_path;
        this.time_stamp = time_stamp;

    }

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {

        this.nid = nid;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImagePath() {
        return image_path;
    }

    public void setImagePath(String image_path
    ) {

        this.image_path = image_path;
    }
    public String getTime_stamp() {
        return time_stamp;
    }

    public void setTime_stamp(String time_stamp
    ) {

        this.time_stamp = time_stamp;
    }
}
