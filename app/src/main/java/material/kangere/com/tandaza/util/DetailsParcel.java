package material.kangere.com.tandaza.util;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Map;

/**
 * This is a serializable class that is used to send data between fragments or activities
 *
 *
 * @author Paul Murage
 */
public class DetailsParcel implements Parcelable {

    private Map map;

    /**
     * Constructor to create DetailsParcel class
     * @param map - stores data to be seriliased and sent to activity or Fragment
     */
    public DetailsParcel(Map map){
        this.map = map;
    }

    private DetailsParcel(Parcel in){
     this.map = in.readHashMap(String.class.getClassLoader());
    }

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public static final Parcelable.Creator<DetailsParcel> CREATOR = new Parcelable.Creator<DetailsParcel>(){
        @Override
        public DetailsParcel createFromParcel(Parcel source) {
            return new DetailsParcel(source);
        }

        @Override
        public DetailsParcel[] newArray(int size) {
            return new DetailsParcel[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeMap(map);
    }

    @Override
    public int describeContents() {
        return 0;
    }


}
