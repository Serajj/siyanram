package com.verbosetech.weshare.listener;

import com.verbosetech.weshare.model.Message;

public interface OnMessageItemClick {
    void OnMessageClick(Message message, int position);

    void OnMessageLongClick(Message message, int position);
}
