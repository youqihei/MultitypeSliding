package multitypesliding.privatecom.wwei.multitypesliding.bean;

/**
 * Created by Administrator on 2018/10/24.
 */

public class Beauty {
    /**
     * 名字
     */
    private String name;
    /**
     * 图片id
     */
    private int  imageId;

    public Beauty(String name,int imageId) {
        this.name = name;
        this.imageId = imageId;
    }

    public String getName() {
        return name;
    }

    public int  getImageId() {
        return imageId;
    }
}
