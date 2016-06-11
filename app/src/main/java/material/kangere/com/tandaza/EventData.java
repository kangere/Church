package material.kangere.com.tandaza;

/**
 * Created by user on 12/17/2015.
 */
public class EventData {
    public EventData(){

    }


    private String title,date,time,ministry,venue,description,posterpath;

    public String getMinistry() {
        return ministry;
    }

    public void setMinistry(String ministry) {
        this.ministry = ministry;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }
    public String getPosterpath() {
        return posterpath;
    }

    public void setPosterpath(String posterpath) {
        this.posterpath = posterpath;
    }
}

