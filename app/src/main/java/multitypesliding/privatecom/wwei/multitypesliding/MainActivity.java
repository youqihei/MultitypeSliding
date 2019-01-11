package multitypesliding.privatecom.wwei.multitypesliding;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import multitypesliding.privatecom.wwei.multitypesliding.adapter.Chat_FrameLayoutAdapter;
import multitypesliding.privatecom.wwei.multitypesliding.adapter.MainAdapter;
import multitypesliding.privatecom.wwei.multitypesliding.bean.Beauty;

public class MainActivity extends AppCompatActivity {

      @BindView(R.id.recycle_view)
    RecyclerView recyclerView;
    private List<Beauty> data = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Test();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
      //  使用瀑布流布局,第一个参数 spanCount 列数,第二个参数 orentation 排列方向
        StaggeredGridLayoutManager recyclerViewLayoutManager =  new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
       // 线性布局Manager
//        LinearLayoutManager recyclerViewLayoutManager = new LinearLayoutManager(this);
//        recyclerViewLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //网络布局Manager
//        GridLayoutManager recyclerViewLayoutManager = new GridLayoutManager(this, 3);
        //给recyclerView设置LayoutManager
        recyclerView.setLayoutManager(recyclerViewLayoutManager);
        initData();
        MainAdapter adapter = new MainAdapter(data, this);
        //设置adapter
        recyclerView.setAdapter(adapter);
    }

    /**
     * 生成一些数据添加到集合中
     */
    public  void Test()
    {

    }

    private void initData() {
        Beauty beauty;
            beauty = new Beauty("子布局Item继承FrameLayout方式侧滑",R.color.colorAccent);
            data.add(beauty);
        beauty = new Beauty("子布局继承LinearLayout方式侧滑",R.color.green);
        data.add(beauty);
        beauty = new Beauty("FrameLayout方式侧滑",R.color.yello);
        data.add(beauty);
        beauty = new Beauty("FrameLayout方式侧滑",R.color.all_types_dialog_txt);
        data.add(beauty);
//            beauty = new Beauty("美女" + i,  "http://img.my.csdn.net/uploads/201309/01/1378037235_7476.jpg");
//            data.add(beauty);
//            beauty = new Beauty("美女" + i, "http://img.my.csdn.net/uploads/201309/01/1378037235_9280.jpg");
//            data.add(beauty);
//            beauty = new Beauty("美女" + i, "http://img.my.csdn.net/uploads/201309/01/1378037234_3539.jpg");
//            data.add(beauty);
//            beauty = new Beauty("美女" + i, "http://img.my.csdn.net/uploads/201309/01/1378037234_6318.jpg");
//            data.add(beauty);
//            beauty = new Beauty("美女" + i,  "http://img.my.csdn.net/uploads/201309/01/1378037194_2965.jpg");
//            data.add(beauty);
//            beauty = new Beauty("美女" + i,"http://img.my.csdn.net/uploads/201309/01/1378037193_1687.jpg");
//            data.add(beauty);
//            beauty = new Beauty("美女" + i, "http://img.my.csdn.net/uploads/201309/01/1378037193_1286.jpg");
//            data.add(beauty);
//            beauty = new Beauty("美女" + i,"http://img.my.csdn.net/uploads/201309/01/1378037192_8379.jpg");
//            data.add(beauty);
//            beauty = new Beauty("美女" + i,    "http://img.my.csdn.net/uploads/201309/01/1378037178_9374.jpg");
//            data.add(beauty);

    }



}
