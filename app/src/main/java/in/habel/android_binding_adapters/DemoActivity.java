package in.habel.android_binding_adapters;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;

import in.habel.chat_adapters.DemoChatModel;
import in.habel.chat_adapters.baseadapter.RecyclerChatAdapter;
import in.habel.chat_adapters.databinding.ListChatIncomingBinding;
import in.habel.chat_adapters.databinding.ListChatOutgoingBinding;
import in.habel.chat_adapters.interfaces.RecyclerChatCallback;

public class DemoActivity extends AppCompatActivity {

    ArrayList<DemoChatModel> dataSet;
    RecyclerChatAdapter<DemoChatModel, ListChatIncomingBinding, ListChatOutgoingBinding> adapter;
    Thread t;
    private Runnable runnable;

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        runnable = new Runnable() {
            @Override
            public void run() {
                while (true) {
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
                        break;
                    }
                }
            }
        };
        dataSet = MockChatData.getChatMockDataList();
        try {
            adapter = new RecyclerChatAdapter(
                    recyclerView, dataSet,
                    R.layout.list_chat_incoming, R.layout.list_chat_outgoing,
                    new RecyclerChatCallback<ListChatIncomingBinding, ListChatOutgoingBinding, DemoChatModel>() {

                        @Override
                        public void bindData(ListChatIncomingBinding incomingBinder, ListChatOutgoingBinding outgoingBinder, DemoChatModel model) {
                            if (incomingBinder != null) {
                                incomingBinder.setChat(model);
                            }
                            if (outgoingBinder != null) {
                                outgoingBinder.setChat(model);

                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
        //   recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onResume() {
        super.onResume();
        t = new Thread(runnable);
        t.start();
    }

    @Override
    protected void onPause() {
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
