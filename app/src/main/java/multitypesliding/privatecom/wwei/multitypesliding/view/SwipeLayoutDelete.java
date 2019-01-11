package multitypesliding.privatecom.wwei.multitypesliding.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by Administrator on 2018/11/29.
 */

public class SwipeLayoutDelete extends LinearLayout {
    View contentView;
    View deleteView;
    public  SwipeLayoutDelete(Context context)
    {
        this(context,null);
    }
    public  SwipeLayoutDelete(Context context, AttributeSet attributeSet)
    {
        this(context,null,0);
    }
    public SwipeLayoutDelete(Context context, AttributeSet attributeSet, int defStyleAtt)
    {
        super(context,attributeSet,defStyleAtt);

    }
    @Override
    public void onFinishInflate()
    {
        super.onFinishInflate();

    }
    @Override
    public void onSizeChanged(int width,int height,int oldw,int oldh)
    {
        super.onSizeChanged(width,height,oldw,oldh);
        contentView = getChildAt(0);
        deleteView = getChildAt(1);

    }
    @Override
    public void onLayout(boolean changed,int left,int top,int right,int bottom)
    {
        super.onLayout(changed,left,top,right,bottom);

    }
}

