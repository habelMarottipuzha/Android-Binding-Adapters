package in.habel.android_binding_adapters;

import android.util.Log;

import java.util.ArrayList;

import in.habel.models.DemoChatModel;

/**
 * Created by habel on 23/4/17.
 */

class MockChatData {
    private static final NonsenseGenerator generator = NonsenseGenerator.getInstance();
    private static ArrayList<DemoChatModel> chatData;

    static ArrayList<DemoChatModel> getChatMockDataList() {
        if (chatData == null) {
            chatData = new ArrayList<>();
            //   chatData.add(new DemoChatModel("Hi, this is Mia", "Mia", System.currentTimeMillis()));
            return chatData;
        }
        return chatData;
    }

    static DemoChatModel getChatMockData() {
        return new DemoChatModel(generator.makeText(2), generator.getRandomThing(), System.currentTimeMillis());
    }

    static ArrayList<DemoChatModel> filter(String search) {
        ArrayList<DemoChatModel> chatData1 = new ArrayList<>();
        int searchListLength = chatData.size();
        for (int i = 0; i < searchListLength; i++) {
            if (chatData.get(i).getMessage().contains(search)) {
                chatData1.add(chatData.get(i));
            }
        }
        Log.d("MockChatData", "filterQuery : " + search + "   return : " + chatData1.size());
        return chatData1;
    }

    static void clear() {
        chatData.clear();
    }
}
