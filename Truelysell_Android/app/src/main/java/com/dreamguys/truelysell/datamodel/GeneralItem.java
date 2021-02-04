package com.dreamguys.truelysell.datamodel;

import com.dreamguys.truelysell.datamodel.Phase3.DAOChatDetails;

/**
 * Created by Hari on 15-05-2018.
 */

public class GeneralItem extends ListItem {

    private DAOChatDetails.ChatHistory chatList;


    public DAOChatDetails.ChatHistory getChatList() {
        return chatList;
    }

    public void setChatList(DAOChatDetails.ChatHistory chatList) {
        this.chatList = chatList;
    }

    @Override
    public int getType() {
        return TYPE_GENERAL;
    }
}
