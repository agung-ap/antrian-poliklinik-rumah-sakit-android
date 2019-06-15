package id.developer.rs_thamrin.model;

public class MenuItemModel {
    private int id;
    private String title;
    private int imageSource;

    public MenuItemModel(int id, String title, int imageSource) {
        this.id = id;
        this.title = title;
        this.imageSource = imageSource;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImageSource() {
        return imageSource;
    }

    public void setImageSource(int imageSource) {
        this.imageSource = imageSource;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
