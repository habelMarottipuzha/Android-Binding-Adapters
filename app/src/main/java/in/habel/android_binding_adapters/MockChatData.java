package in.habel.android_binding_adapters;

import java.util.ArrayList;

import in.habel.chat_adapters.DemoChatModel;

/**
 * Created by habel on 23/4/17.
 */

class MockChatData {
    private static ArrayList<DemoChatModel> chatData;
    private static NonsenseGenerator generator = NonsenseGenerator.getInstance();


    static ArrayList<DemoChatModel> getChatMockDataList() {
        if (chatData == null) {
            chatData = new ArrayList<>();
            chatData.add(new DemoChatModel("Hi, this is Mia", "Mia", System.currentTimeMillis()));
            return chatData;
        }
        DemoChatModel model = new DemoChatModel(generator.makeText(2), System.currentTimeMillis());
        model.setUser(generator.getRandomThing());
        chatData.add(model);
        return chatData;
    }

    static DemoChatModel getChatMockData() {
        return new DemoChatModel(generator.makeText(2), generator.getRandomThing(), System.currentTimeMillis());
    }
}
