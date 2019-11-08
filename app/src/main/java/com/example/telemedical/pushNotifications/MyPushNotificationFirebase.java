package com.example.telemedical.pushNotifications;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessagingService;

public class MyPushNotificationFirebase extends FirebaseInstanceIdService {
    private static String REG_TOOKEN="REG_TOKKEN";


    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String recent_Token= FirebaseInstanceId.getInstance().getToken();
    }
}
