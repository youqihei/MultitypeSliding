package multitypesliding.privatecom.wwei.multitypesliding.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import multitypesliding.privatecom.wwei.multitypesliding.R;
import multitypesliding.privatecom.wwei.multitypesliding.adapter.Chat_FrameLayoutAdapter;
import multitypesliding.privatecom.wwei.multitypesliding.adapter.Chat_LinearLayoutAdapter;

/**
 * Created by Administrator on 2018/11/27.
 */

public class LinearLayoutSwipeActivity extends AppCompatActivity {
    @BindView(R.id.recycle_view)
    RecyclerView recyclerView;

    private Chat_LinearLayoutAdapter adapter;
    private List<String> list = new LinkedList<>();
    private  Chat_LinearLayoutAdapter.DeleteAndStick callback;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lls_main);
        ButterKnife.bind(this);
        initView();
        initData();
    }
    public void initView()
    {
        LinearLayoutManager recyclerViewLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(recyclerViewLayoutManager);
        adapter = new Chat_LinearLayoutAdapter(list,this,callback);
      recyclerView.setAdapter(adapter);
      adapter.notifyDataSetChanged();
    }
    public void initData()
    {
        callback = new Chat_LinearLayoutAdapter.DeleteAndStick() {
            @Override
            public void Delete(int position) {

            }
        };
        for(int i=0;i<20;i++)
        {
            list.add("继承linearlayout方式的数据"+i);
        }
        adapter.notifyDataSetChanged();
    }
}
