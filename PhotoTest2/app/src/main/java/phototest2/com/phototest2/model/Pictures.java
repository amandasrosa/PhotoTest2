package phototest2.com.phototest2.model;

/**
 * Created by Amanda on 08/12/2017.
 */

public class Pictures {
    private int id;
    private String photoPath;
    private String photographerName;
    private boolean selected = false;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public String getPhotographerName() {
        return photographerName;
    }

    public void setPhotographerName(String photographerName) {
        this.photographerName = photographerName;
    }
    public boolean selected(){
        return selected;
    }
    public void turnSelected(){
        selected = !selected;
    }

    @Override
    public String toString() {
        return "Number 0" + getId();
    }
}
