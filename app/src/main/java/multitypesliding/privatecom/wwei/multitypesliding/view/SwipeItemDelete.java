package multitypesliding.privatecom.wwei.multitypesliding.view;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import multitypesliding.privatecom.wwei.multitypesliding.utils.SwipeDeleteManager;

/**
 * Created by Administrator on 2018/10/29.
 */

public class SwipeItemDelete extends FrameLayout{
    View contentView;
    View deleteView;
    private int contentHeight;
    private int contentWidth;
    private int deleteWidth;
    private ViewDragHelper viewDragHelper;
    private static boolean isTouching;
    enum State {
        close, open
    }
    //默认状态是关闭
    private State state = State.close;
    public  SwipeItemDelete(Context context)
    {
        this(context,null);
    }
    public SwipeItemDelete(Context context, AttributeSet attrs)
    {
        this(context,null,0);
    }
    public SwipeItemDelete(Context context,AttributeSet attrs,int defStyleAttr)
    {
        super(context,attrs,defStyleAttr);
        init();
    }
    private void init()
    {
       viewDragHelper = ViewDragHelper.create(this,1.0f,callback);

    }
//    public boolean isIos()
//    {
//        return  isIos;
//    }
//    public  SwipeItemDelete setIos(boolean ios)
//    {
//        isIos = ios;
//        return this;
//    }
    private ViewDragHelper.Callback callback = new ViewDragHelper.Callback()
    {
        @Override
        public boolean tryCaptureView(View child,int pointerId)
        {
           return  child == contentView||child ==deleteView;
        }
        @Override
        public  int getViewHorizontalDragRange(View child)
        {
            return  deleteWidth;
        }
        @Override
        public  int clampViewPositionHorizontal(View child,int left,int dx)
        {
            if(child== contentView)
            {
              if(left>0) left = 0;
              if(left<-deleteWidth)
                  left = -deleteWidth;
            }
            else if(child==deleteView)
            {
               if(left>contentWidth)
                   left = contentWidth;
               if(left<contentWidth - deleteWidth)
                   left = contentWidth - deleteWidth;
            }
            return  left;
        }
        @Override
        public void onViewPositionChanged(View changedView,int letf,int top,int dx,int dy)
        {
            super.onViewPositionChanged(changedView,letf,top,dx,dy);
            if(changedView==contentView)
            {
               deleteView.layout(deleteView.getLeft()+dx,deleteView.getTop()+dy,deleteView.getRight()+dx,deleteView.getBottom()+dy);
            }else if(changedView==deleteView)
            {
               contentView.layout(contentView.getLeft()+dx,contentView.getTop()+dy,contentView.getRight()+dx,contentView.getBottom()+dy);
            }
//            if(contentView.getLeft()!=0&&state==State.open)
//            {
//                SwipeDeleteManager.getInstance().setSwipeItemDelete(SwipeItemDelete.this);
//             }
//            else
//            {
//               SwipeDeleteManager.getInstance().clear();
//            }
            if(contentView.getLeft()==0&&state!=State.close)
                state = State.close;
            else if(contentView.getLeft()==-deleteWidth&&state!=State.open)
                state = State.open;
        }
        @Override
        public void onViewReleased(View releasedChild,float xvel,float yvel)
        {
            super.onViewReleased(releasedChild,xvel,yvel);
            //速度达到一定的值的时候直接打开或关闭
            if(xvel<-2000)
            {
                    open();
              return;
            }
            if(xvel>2000)
            {
                close();
                return;
            }
            if(contentView.getLeft()>-deleteWidth/2)
            {
                close();
            }
            else
            {
                    //如果打开的不是当前的item 关闭
                    open();
            }
        }
    };
    public void open()
    {
        viewDragHelper.smoothSlideViewTo(contentView,-deleteWidth,contentView.getTop());
        state = State.open;
        SwipeDeleteManager.getInstance().setSwipeItemDelete(SwipeItemDelete.this);
       ViewCompat.postInvalidateOnAnimation(SwipeItemDelete.this);
    }
    public void close()
    {

        viewDragHelper.smoothSlideViewTo(contentView,0,contentView.getTop());
        state = State.close;
        SwipeDeleteManager.getInstance().clear();
        ViewCompat.postInvalidateOnAnimation(SwipeItemDelete.this);
    }
    @Override
    public void computeScroll()
    {
        if(viewDragHelper.continueSettling(true))
        {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }
    @Override
    public  void onFinishInflate()
    {
       super.onFinishInflate();
       contentView = getChildAt(0);
       deleteView = getChildAt(1);
    }

    @Override
    public void onSizeChanged(int w,int h,int oldw,int oldh)
    {
        super.onSizeChanged(w,h,oldw,oldh);
        contentHeight = contentView.getMeasuredHeight();
        contentWidth = contentView.getMeasuredWidth();
        deleteWidth = deleteView.getMeasuredWidth();
    }
    @Override
    public void onLayout(boolean changed,int left,int top,int right,int bottom)
    {
      super.onLayout(changed,left,top,right,bottom);
      contentView.layout(0,0,contentWidth,contentHeight);
      deleteView.layout(contentWidth,0,contentWidth+deleteWidth,contentHeight);
    }
    float downx;
    float downy;
    float movex;
    float movey;
    @Override
    public boolean dispatchTouchEvent(MotionEvent event)
    {
        final int action = event.getActionMasked();
        switch (action)
        {
            case MotionEvent.ACTION_DOWN:
                downx = event.getX();
                downy = event.getY();
                if(isTouching)
                {
                    return false;
                }
                else
                {
                    isTouching = true;
                }
                if(SwipeDeleteManager.getInstance().getSwipeItemDelete()!=null)
                {
                    //如果打开的不是当前的item 关闭
                    if(SwipeDeleteManager.getInstance().getSwipeItemDelete()!=this)
                    SwipeDeleteManager.getInstance().close();
                    requestDisallowInterceptTouchEvent(true);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                movex = event.getX();
                movey = event.getY();
                float dx = Math.abs(movex -downx);
                float dy = Math.abs(movex -downy);
                    //如果 x距离大于y 则不允许父控件拦截事件
                    //为了在水平滑动中禁止父类ListView等再竖直滑动
                    if (Math.abs(dx) > 10 || Math.abs(getScrollX()) > 10) {//2016 09 29 修改此处，使屏蔽父布局滑动更加灵敏，
                requestDisallowInterceptTouchEvent(true);
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                isTouching = false;
                break;
        }

        return  super.dispatchTouchEvent(event);
    }
     @Override
    public boolean onInterceptTouchEvent(MotionEvent event)
     {
         boolean value = viewDragHelper.shouldInterceptTouchEvent(event);
       return value;
     }
    @Override
    protected void onDetachedFromWindow() {
        if (this == SwipeDeleteManager.getInstance().getSwipeItemDelete()) {
            state = State.close;
            SwipeDeleteManager.getInstance().clear();
        }
        super.onDetachedFromWindow();
    }
     @Override
    public boolean onTouchEvent(MotionEvent event)
     {
         viewDragHelper.processTouchEvent(event);
         return true;
     }
}
