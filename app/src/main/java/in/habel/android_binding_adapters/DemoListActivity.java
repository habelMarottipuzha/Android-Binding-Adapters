package in.habel.android_binding_adapters;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import in.habel.chat_adapters.RecyclerAdapter;
import in.habel.chat_adapters.databinding.ListChatIncomingBinding;
import in.habel.interfaces.RecyclerCallback;
import in.habel.models.DemoChatModel;

public class DemoListActivity extends AppCompatActivity implements EditTextWithDrawable.DrawableClickListener {

    ArrayList<DemoChatModel> dataSet;
    RecyclerAdapter<DemoChatModel, ListChatIncomingBinding> adapter;
    EditTextWithDrawable txtSend;
    RecyclerView recyclerView;
    String searchQuery = "";
    ExecutorService taskExecutor;
    Random random;

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        random = new Random(123987);
        recyclerView = findViewById(R.id.recyclerView);
        txtSend = findViewById(R.id.txtSend);
        txtSend.setDrawableClickListener(this);
        dataSet = MockChatData.getChatMockDataList();
        MockChatData.clear();
        for (int i = 0; i < 1; i++) {
            dataSet.add(new DemoChatModel("mx" + i, 0));
        }
        updateAdapter();
        setSearch(findViewById(R.id.searchView));
    }

    @SuppressWarnings("unchecked")
    private void updateAdapter() {
        try {
            adapter = new RecyclerAdapter(
                    recyclerView, MockChatData.filter(searchQuery),
                    R.layout.list_chat_incoming,
                    new RecyclerCallback<ListChatIncomingBinding, DemoChatModel>() {

                        @Override
                        public void bindData(ListChatIncomingBinding binder, DemoChatModel model) {
                            binder.setChat(model);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setSearch(SearchView searchView) {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchQuery = newText;
                adapter.refresh(MockChatData.filter(newText));
                return false;
            }
        });
    }

    private void insertRandomMessages(int count) {
        for (int i = 0; i < count; i++) {
            if (taskExecutor == null || taskExecutor.isShutdown() || taskExecutor.isTerminated())
                taskExecutor = Executors.newFixedThreadPool(4);
            taskExecutor.execute(() -> {
                try {
                    Thread.sleep(random.nextInt(10000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(() -> adapter.insert(MockChatData.getChatMockData()));
            });

        }
        taskExecutor.shutdown();
    }

    @Override
    protected void onResume() {
        super.onResume();
        insertRandomMessages(12);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        startActivity(new Intent(this, DemoChatActivity.class));
        finish();
        return super.onOptionsItemSelected(item);
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
