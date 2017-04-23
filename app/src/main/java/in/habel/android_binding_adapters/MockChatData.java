package in.habel.android_binding_adapters;

import java.util.ArrayList;

/**
 * Created by habel on 23/4/17.
 */

class MockChatData {
    private static ArrayList<DemoChatModel> chatData;
    private static NonsenseGenerator generator = NonsenseGenerator.getInstance();


    static ArrayList<DemoChatModel> getChatMockDataList() {
        if (chatData == null) chatData = new ArrayList<>();
        DemoChatModel model = new DemoChatModel(generator.makeText(2), System.currentTimeMillis());
        chatData.add(model);
        return chatData;
    }

    static DemoChatModel getChatMockData() {
        return new DemoChatModel(generator.makeText(2), System.currentTimeMillis());
    }
}
