package multitypesliding.privatecom.wwei.multitypesliding.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.IntentService;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.LinearLayout;
import android.widget.Toast;

import multitypesliding.privatecom.wwei.multitypesliding.R;

/**
 * Created by Administrator on 2018/12/4.
 */

public class SlideDeleteView extends ViewGroup {
    private int mWidth;
    private int mHeight;
    private View contentView;
    private View deleteView;
    //首次触摸
    private int downX;
    //位移变量
    private int leftX;
    //右滑删除功能的开关,默认开
    private boolean isSwipeEnable = true;
    //IOS、QQ式交互，默认开
    private boolean isIos = true;
    //左滑右滑的开关,默认左滑打开菜单
    private  boolean isLeftSwipe = true;
    private boolean isUnMoved = true;
    //qq截图 3倍图的大小
    private static int defaultOptionsWidth = 100 / 3;
    private static int defaultOptionsHeight = 80 / 3;
    //存储的是当前正在展开的View
    private static SlideDeleteView mViewCache;
    //内容的宽度
    private int optionViewWidth;
    //option选项的宽度
    private int contentViewWidth;
    //上一次的xy
    private PointF mLastP = new PointF();
    //up-down的坐标，判断是否是滑动，如果是，则屏蔽一切点击事件
    private PointF mFirstP = new PointF();
    //用来处理单击事件是否在内容区域内
    private RectF contentRectF;
    private RectF deleteRectF;
    private IntentService intentService;
    private int mScaleTouchSlop;//为了处理单击事件的冲突
    private int mMaxVelocity;//计算滑动速度用
    private boolean isUserSwiped;
    private static boolean isTouching;
    public SlideDeleteView(Context context) {
        this(context, null);
    }

    public SlideDeleteView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideDeleteView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //init type arrays
        init(context, attrs, defStyleAttr);
    }
    private void init(Context context, AttributeSet attrs, int defStyleAttr)
        {
            mScaleTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
            mMaxVelocity = ViewConfiguration.get(context).getScaledMaximumFlingVelocity();
            TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SwipeDeleteView, defStyleAttr, 0);
            int count = ta.getIndexCount();
            for (int i = 0; i < count; i++) {
                int attr = ta.getIndex(i);
                //如果引用成AndroidLib 资源都不是常量，无法使用switch case
                if (attr == R.styleable.SwipeDeleteView_swipeEnable) {
                    isSwipeEnable = ta.getBoolean(attr, true);
                } else if (attr == R.styleable.SwipeDeleteView_ios) {
                    isIos = ta.getBoolean(attr, true);
                } else if (attr == R.styleable.SwipeDeleteView_leftSwipe) {
                    isLeftSwipe = ta.getBoolean(attr, true);
                }
            }
            ta.recycle();
        }
    //右侧菜单宽度总和(最大滑动距离)
    private int mRightMenuWidths;
    private View mContentView;//2016 11 13 add ，存储contentView(第一个View)

    //滑动判定临界值（右侧菜单宽度的40%） 手指抬起时，超过了展开，没超过收起menu
    private int mLimit;
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //Log.d(TAG, "onMeasure() called with: " + "widthMeasureSpec = [" + widthMeasureSpec + "], heightMeasureSpec = [" + heightMeasureSpec + "]");
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final boolean measureMatchParentChildren = MeasureSpec.getMode(heightMeasureSpec)!= MeasureSpec.EXACTLY;
        boolean isNeedMeasureChildHeight = false;
        int contentWidth = 0;// add,适配GridLayoutManager，将以第一个子Item(即ContentItem)的宽度为控件宽度
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            //令每一个子View可点击，从而获取触摸事件
            childView.setClickable(true);
            if (childView.getVisibility() != GONE) {
                //后续计划加入上滑、下滑，则将不再支持Item的margin
                measureChild(childView, widthMeasureSpec, heightMeasureSpec);
                //measureChildWithMargins(childView, widthMeasureSpec, 0, heightMeasureSpec, 0);
                final MarginLayoutParams lp = (MarginLayoutParams) childView.getLayoutParams();
                mHeight = Math.max(mHeight, childView.getMeasuredHeight()/* + lp.topMargin + lp.bottomMargin*/);
                if (measureMatchParentChildren && lp.height == LayoutParams.MATCH_PARENT) {
                    isNeedMeasureChildHeight = true;
                }
                if (i > 0) {//第一个布局是Left item，从第二个开始才是RightMenu
                    mRightMenuWidths += childView.getMeasuredWidth();
                } else {
                    mContentView = childView;
                    contentWidth = childView.getMeasuredWidth();
                }
            }
        }
        setMeasuredDimension(getPaddingLeft()+getPaddingRight()+contentWidth,mHeight+getPaddingTop()+getPaddingBottom());
        mLimit = mRightMenuWidths * 4 / 10;//滑动判断的临界值
        //Log.d(TAG, "onMeasure() called with: " + "mRightMenuWidths = [" + mRightMenuWidths);
        if (isNeedMeasureChildHeight) {//如果子View的height有MatchParent属性的，设置子View高度
            forceUniformHeight(childCount, widthMeasureSpec);
        }
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }
    /**
     * 给MatchParent的子View设置高度
     *
     * @param count
     * @param widthMeasureSpec
     * @see android.widget.LinearLayout# 同名方法
     */
    private void forceUniformHeight(int count, int widthMeasureSpec) {
        // Pretend that the linear layout has an exact size. This is the measured height of
        // ourselves. The measured height should be the max height of the children, changed
        // to accommodate the heightMeasureSpec from the parent
        int uniformMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredHeight(),
                MeasureSpec.EXACTLY);//以父布局高度构建一个Exactly的测量参数
        for (int i = 0; i < count; ++i) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
                if (lp.height == LayoutParams.MATCH_PARENT) {
                    // Temporarily force children to reuse their old measured width
                    // FIXME: this may not be right for something like wrapping text?
                    int oldWidth = lp.width;//measureChildWithMargins 这个函数会用到宽，所以要保存一下
                    lp.width = child.getMeasuredWidth();
                    // Remeasure with new dimensions
                    measureChildWithMargins(child, widthMeasureSpec, 0, uniformMeasureSpec, 0);
                    lp.width = oldWidth;
                }
            }
        }
    }
    @Override
    protected  void onLayout(boolean changed,int l,int t,int r,int b)
    {
        int childCount = getChildCount();
        int left = 0 + getPaddingLeft();
        int right = 0 + getPaddingLeft();
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            if (childView.getVisibility() != GONE) {
                if (i == 0) {//第一个子View是内容 宽度设置为全屏
                    childView.layout(left, getPaddingTop(), left + childView.getMeasuredWidth(), getPaddingTop() + childView.getMeasuredHeight());
                    left = left + childView.getMeasuredWidth();
                } else {
                    if (isLeftSwipe) {
                        childView.layout(left, getPaddingTop(), left + childView.getMeasuredWidth(), getPaddingTop() + childView.getMeasuredHeight());
                        left = left + childView.getMeasuredWidth();
                    } else {
                        childView.layout(right - childView.getMeasuredWidth(), getPaddingTop(), right, getPaddingTop() + childView.getMeasuredHeight());
                        right = right - childView.getMeasuredWidth();
                    }

                }
            }
        }
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev)
    {
         if(isSwipeEnable)
         {
             acquireVelocityTracker(ev);
             final VelocityTracker verTracker = mVelocityTracker;
             switch (ev.getAction())
             {
                 case  MotionEvent.ACTION_DOWN:
                     isUserSwiped = false;
                     isUnMoved = true;
                     if(isTouching)
                     {
                         return false;
                     }
                     else
                     {
                         isTouching = true;
                     }
                     mLastP.set(ev.getRawX(), ev.getRawY());
                     mFirstP.set(ev.getRawX(), ev.getRawY());
                      if(mViewCache!=null)
                      {
                          if(mViewCache!=this)
                          {
                                  mViewCache.smoothClose();
                          }
                          getParent().requestDisallowInterceptTouchEvent(true);
                      }
                     break;
                 case MotionEvent.ACTION_MOVE:
                     float gap = mLastP.x - ev.getRawX();
                     //为了在水平滑动中禁止父类ListView等再竖直滑动
                     if (Math.abs(gap) > 10 || Math.abs(getScrollX()) > 10) {//2016 09 29 修改此处，使屏蔽父布局滑动更加灵敏，
                         getParent().requestDisallowInterceptTouchEvent(true);
                     }
                     if (Math.abs(gap) > mScaleTouchSlop) {
                         isUnMoved = false;
                     }
                     scrollBy((int)gap,0);
                      if(getScrollX()<0)
                      {
                         scrollTo(0,0);
                      }
                     if (getScrollX() > mRightMenuWidths) {
                         scrollTo(mRightMenuWidths, 0);
                     }
                     mLastP.set(ev.getRawX(), ev.getRawY());
                     break;
                 case MotionEvent.ACTION_UP:
                 case MotionEvent.ACTION_CANCEL:
                     if (Math.abs(ev.getRawX() - mFirstP.x) > mScaleTouchSlop) {
                         isUserSwiped = true;
                     }
                     verTracker.computeCurrentVelocity(1000,mMaxVelocity);
                     final float velocityX = verTracker.getXVelocity();
                     if(Math.abs(velocityX) > 1000)
                     {
                       if(velocityX<-1000)
                       {
                           smoothExpand();
                       }
                       else
                       {
                           smoothClose();
                       }
                     }
                     else
                     {
                        if(Math.abs(getScrollX())>mLimit)
                        {
                            smoothExpand();
                        }
                        else
                        {
                            smoothClose();
                        }
                     }

                     //释放
                     releaseVelocityTracker();
                     isTouching = false;
                     break;
             }
         }
        return super.dispatchTouchEvent(ev);
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev)
    {
       if(isSwipeEnable)
       {
         switch (ev.getAction())
             {
                 case MotionEvent.ACTION_DOWN:
                     break;
                 case MotionEvent.ACTION_MOVE:
                     if(Math.abs(ev.getRawX()-mFirstP.x)>mScaleTouchSlop)
                     {
                         return  true;
                     }
                     break;
                 case MotionEvent.ACTION_UP:
                    if(getScrollX()>mScaleTouchSlop)
                    {
                        if(ev.getX()<getWidth()-getScrollX())
                        {
                            if (isUnMoved) {
                                smoothClose();
                            }
                            return true;//true表示拦截
                        }
                    }
                     break;
             }
       }
        return super.onInterceptTouchEvent(ev);
    }
    private VelocityTracker mVelocityTracker;//滑动速度变量
    /**
     * @param event 向VelocityTracker添加MotionEvent
     * @see VelocityTracker#obtain()
     * @see VelocityTracker#addMovement(MotionEvent)
     */
    private void acquireVelocityTracker(final MotionEvent event) {
        if (null == mVelocityTracker) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }
    private void releaseVelocityTracker() {
        if (null != mVelocityTracker) {
            mVelocityTracker.clear();
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }
    public void smoothExpand() {
        //Log.d(TAG, "smoothExpand() called" + this);
        /*mScroller.startScroll(getScrollX(), 0, mRightMenuWidths - getScrollX(), 0);
        invalidate();*/
        //展开就加入ViewCache：
        mViewCache = SlideDeleteView.this;

        //2016 11 13 add 侧滑菜单展开，屏蔽content长按
        if (null != mContentView) {
            mContentView.setLongClickable(false);
        }
        cancelAnim();
        mExpandAnim = ValueAnimator.ofInt(getScrollX(), isLeftSwipe ? mRightMenuWidths : -mRightMenuWidths);
        mExpandAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                scrollTo((Integer) animation.getAnimatedValue(), 0);
            }
        });
        mExpandAnim.setInterpolator(new OvershootInterpolator());
        mExpandAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                isExpand = true;
            }
        });
        mExpandAnim.setDuration(300).start();
    }
    /**
     * 平滑关闭
     */
    public void smoothClose() {
        //Log.d(TAG, "smoothClose() called" + this);
/*        mScroller.startScroll(getScrollX(), 0, -getScrollX(), 0);
        invalidate();*/
        mViewCache = null;

        //2016 11 13 add 侧滑菜单展开，屏蔽content长按
        if (null != mContentView) {
            mContentView.setLongClickable(true);
        }

        cancelAnim();
        mCloseAnim = ValueAnimator.ofInt(getScrollX(), 0);
        mCloseAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                scrollTo((Integer) animation.getAnimatedValue(), 0);
            }
        });
        mCloseAnim.setInterpolator(new AccelerateInterpolator());
        mCloseAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                isExpand = false;

            }
        });
        mCloseAnim.setDuration(300).start();
        //LogUtils.d(TAG, "smoothClose() called with:getScrollX() " + getScrollX());
    }

    /**
     * 平滑展开
     */
    private ValueAnimator mExpandAnim, mCloseAnim;
    private boolean isExpand;//代表当前是否是展开状态 2016 11 03 add
    /**
     * 每次执行动画之前都应该先取消之前的动画
     */
    private void cancelAnim() {
        if (mCloseAnim != null && mCloseAnim.isRunning()) {
            mCloseAnim.cancel();
        }
        if (mExpandAnim != null && mExpandAnim.isRunning()) {
            mExpandAnim.cancel();
        }
    }
}


