package in.habel.android_binding_adapters;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.animation.OvershootInterpolator;

import java.util.ArrayList;

import in.habel.android_binding_adapters.databinding.ListChatBinding;
import in.habel.chat_adapters.baseadapter.RecyclerChatAdapter;
import in.habel.chat_adapters.baseadapter.interfaces.RecyclerChatCallback;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class DemoActivity extends AppCompatActivity {

    ArrayList<DemoChatModel> dataSet;
    RecyclerChatAdapter<DemoChatModel, ListChatBinding, ListChatBinding> adapter;
    Thread t;
    Handler handler;
    int count;
    private Runnable runnable;
    private RecyclerView recyclerView;
    private boolean update;

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        runnable = new Runnable() {
            @Override
            public void run() {
                while (update) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updateChatData();
                        }
                    });
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        handler = new Handler();
        dataSet = MockChatData.getChatMockDataList();
        adapter = new RecyclerChatAdapter(
                recyclerView, dataSet,
                R.layout.list_chat, R.layout.list_chat,
                new RecyclerChatCallback<ListChatBinding, ListChatBinding, DemoChatModel>() {

                    @Override
                    public void bindData(ListChatBinding incomingBinder, ListChatBinding outgoingBinder, DemoChatModel model) {
                        if (incomingBinder != null) {
                            incomingBinder.setChat(model);
                        }
                        if (outgoingBinder != null) {
                            outgoingBinder.setChat(model);

                        }
                    }
                });
        //   recyclerView.setLayoutManager(new LinearLayoutManager(this));

        SlideInUpAnimator animator = new SlideInUpAnimator(new OvershootInterpolator(1f));
        recyclerView.setItemAnimator(animator);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        update = true;
        t = new Thread(runnable);
        t.start();
    }

    @Override
    protected void onPause() {
        update = false;
        if (t.isAlive())
            try {
                t.stop();
            } catch (Exception ignored) {
            }
        super.onPause();
    }

    private void updateChatData() {
        Log.w(getClass().getSimpleName(), "updateChatData() ");
        // dataSet = MockChatData.getChatMockDataList();
        Log.w(getClass().getSimpleName(), "updateChatData() size : " + dataSet.size());
        adapter.insert(MockChatData.getChatMockData());

        // adapter.refresh();
    }
}
