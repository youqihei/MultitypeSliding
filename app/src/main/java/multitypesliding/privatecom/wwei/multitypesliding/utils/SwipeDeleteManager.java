package multitypesliding.privatecom.wwei.multitypesliding.utils;

import android.util.Log;

import multitypesliding.privatecom.wwei.multitypesliding.view.SwipeItemDelete;

/**
 * Created by Administrator on 2018/11/15.
 */

public class SwipeDeleteManager {
    private SwipeDeleteManager()
    {

    }
    private static SwipeDeleteManager swipeDeleteManager = new SwipeDeleteManager();
    public static SwipeDeleteManager getInstance()
    {
        return swipeDeleteManager;
    }
    private SwipeItemDelete swipeItemDelete;
    public void setSwipeItemDelete(SwipeItemDelete s)
    {
        swipeItemDelete = s;
    }
    public  SwipeItemDelete getSwipeItemDelete()
    {
        return  swipeItemDelete;
    }
    public void clear()
    {
      //  Log.e("sss","clear");
        swipeItemDelete = null;
    }
    public void close()
    {
        if (swipeItemDelete!= null){
            swipeItemDelete.close();
        }
    }
    /**
     * 是否有item已经打开
     */
    public boolean haveOpened() {
        return swipeItemDelete!= null;
    }
    public boolean haveOpened(SwipeItemDelete s)
    {
        return swipeItemDelete!= null&&swipeItemDelete == s;
    }
}
