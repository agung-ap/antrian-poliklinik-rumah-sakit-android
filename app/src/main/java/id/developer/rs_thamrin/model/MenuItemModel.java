package id.developer.rs_thamrin.model;

public class MenuItemModel {
    private String title;
    private int imageSource;

    public MenuItemModel(String title, int imageSource) {
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
}
