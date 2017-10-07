package in.habel.android_binding_adapters;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import in.habel.chat_adapters.RecyclerChatAdapter;
import in.habel.chat_adapters.databinding.ListChatIncomingBinding;
import in.habel.chat_adapters.databinding.ListChatOutgoingBinding;
import in.habel.interfaces.RecyclerChatCallback;
import in.habel.models.DemoChatModel;

public class DemoChatActivity extends AppCompatActivity implements EditTextWithDrawable.DrawableClickListener {

    static Thread t;
    ArrayList<DemoChatModel> dataSet;
    RecyclerChatAdapter<DemoChatModel, ListChatIncomingBinding, ListChatOutgoingBinding> adapter;
    EditTextWithDrawable txtSend;

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        txtSend = findViewById(R.id.txtSend);
        txtSend.setDrawableClickListener(this);
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

                        //@Override
                        public void onUnreadMessageFound(int totalMessages, int unreadMessages) {
                            Log.e(DemoChatActivity.this.getClass().getSimpleName(), "onUnreadMessageFound : " + unreadMessages);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
        //   recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        startActivity(new Intent(this, DemoListActivity.class));
        finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        t = new Thread(() -> {
            for (int i = 0; i < 25; i++) {
                Log.e(DemoChatActivity.this.getClass().getSimpleName(), "onThread : " + t.getId());

                runOnUiThread(this::updateChatData);
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            t.interrupt();
        } catch (Exception ignored) {
        }
    }

    private void updateChatData() {

        adapter.insert(MockChatData.getChatMockData());
    }

    @Override
    public void onClick(DrawablePosition target) {
        if (target == DrawablePosition.RIGHT) {
            String text = txtSend.getText().toString();
            if (text.trim().equalsIgnoreCase("")) return;
            DemoChatModel cur = new DemoChatModel(text, "You", System.currentTimeMillis(), NonsenseGenerator.getInstance().getRandomBool());
            cur.setIsOut(true);
            adapter.insert(cur);
            txtSend.setText("");
            new Handler().postDelayed(() -> adapter.insert(MockChatData.getChatMockData()), 1500);
        }
    }
}
