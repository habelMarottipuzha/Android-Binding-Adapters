package in.habel.android_binding_adapters;

import android.os.Bundle;
import android.os.Handler;
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

    EditTextWithDrawable txtSend;
    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        txtSend = (EditTextWithDrawable) findViewById(R.id.txtSend);
        txtSend.setDrawableClickListener(new EditTextWithDrawable.DrawableClickListener() {
            @Override
            public void onClick(DrawablePosition target) {
                if (target == DrawablePosition.RIGHT) {
                    String text = txtSend.getText().toString();
                    if (text.trim().equalsIgnoreCase("")) return;
                    DemoChatModel cur = new DemoChatModel(text, "You", System.currentTimeMillis());
                    cur.setIsOut(true);
                    adapter.insert(cur);
                    txtSend.setText("");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            adapter.insert(MockChatData.getChatMockData());
                        }
                    }, 1500);
                }
            }
        });
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

                        @Override
                        public void onUnreadMessageFound(int totalMessages, int unreadMessages) {
                            // TODO: 25/4/17 Need to implement
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
        //   recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }


    private void updateChatData() {
        Log.w(getClass().getSimpleName(), "updateChatData() ");
        // dataSet = MockChatData.getChatMockDataList();
        Log.w(getClass().getSimpleName(), "updateChatData() size : " + dataSet.size());
        adapter.insert(MockChatData.getChatMockData());
        // adapter.refresh();
    }
}
