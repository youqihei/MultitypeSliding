package multitypesliding.privatecom.wwei.multitypesliding.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import multitypesliding.privatecom.wwei.multitypesliding.R;
import multitypesliding.privatecom.wwei.multitypesliding.adapter.Chat_FrameLayoutAdapter;
import multitypesliding.privatecom.wwei.multitypesliding.utils.SwipeDeleteManager;

/**
 * Created by Administrator on 2018/10/29.
 */

public class FrameLayoutSwipeActivity extends AppCompatActivity{
//    @BindView(R.id.swipe_refresh)
//    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recycle_view)
    RecyclerView recyclerView;
    Chat_FrameLayoutAdapter frameLayoutAdapter;
    private List<String> represent = new LinkedList<>();
    @Override
    public  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_main);
        ButterKnife.bind(this);
        initView();
        initData();
    }
    public void initView()
    {
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                swipeRefreshLayout.setRefreshing(false);
//            }
//        });
        // 线性布局Manager
        LinearLayoutManager recyclerViewLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(recyclerViewLayoutManager);
        frameLayoutAdapter = new Chat_FrameLayoutAdapter(represent,this,deleteAndStickcallback);
        recyclerView.setAdapter(frameLayoutAdapter);

    }
    private Chat_FrameLayoutAdapter.DeleteAndStick deleteAndStickcallback = new Chat_FrameLayoutAdapter.DeleteAndStick() {
        @Override
        public void Delete(int position) {
            SwipeDeleteManager swipeDeleteManager = SwipeDeleteManager.getInstance();
            swipeDeleteManager.close();
            swipeDeleteManager.clear();
           represent.remove(position);
           recyclerView.setAdapter(frameLayoutAdapter);
           frameLayoutAdapter.notifyDataSetChanged();
        }
    };
    public void initData()
    {
       for(int i=0;i<10;i++)
       {
           represent.add("现在假装这是从服务器上面的数据"+i);
       }

       frameLayoutAdapter.notifyDataSetChanged();
    }
}
