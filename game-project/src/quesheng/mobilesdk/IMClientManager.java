/*
 * Decompiled with CFR 0.152.
 */
package quesheng.mobilesdk;

import net.openmob.mobileimsdk.java.ClientCoreSDK;
import net.openmob.mobileimsdk.java.conf.ConfigEntity;
import quesheng.mobilesdk.event.ChatBaseEventImpl;
import quesheng.mobilesdk.event.ChatTransDataEventImpl;
import quesheng.mobilesdk.event.MessageQoSEventImpl;
import quesheng.util.param;

public class IMClientManager {
    private static String TAG = IMClientManager.class.getSimpleName();
    private static IMClientManager instance = null;
    private boolean init = false;
    private ChatBaseEventImpl baseEventListener = null;
    private ChatTransDataEventImpl transDataListener = null;
    private MessageQoSEventImpl messageQoSListener = null;

    public static IMClientManager getInstance() {
        if (instance == null) {
            instance = new IMClientManager();
        }
        return instance;
    }

    private IMClientManager() {
        this.initMobileIMSDK();
    }

    public void initMobileIMSDK() {
        if (!this.init) {
            ConfigEntity.appKey = "5418023dfd98c579b6001741";
            ConfigEntity.serverIP = param.user.getServer_ip();
            ConfigEntity.serverUDPPort = 7901;
            ConfigEntity.localUDPPort = 0;
            ClientCoreSDK.DEBUG = false;
            this.baseEventListener = new ChatBaseEventImpl();
            this.transDataListener = new ChatTransDataEventImpl();
            this.messageQoSListener = new MessageQoSEventImpl();
            ClientCoreSDK.getInstance().setChatBaseEvent(this.baseEventListener);
            ClientCoreSDK.getInstance().setChatTransDataEvent(this.transDataListener);
            ClientCoreSDK.getInstance().setMessageQoSEvent(this.messageQoSListener);
            this.init = true;
        }
    }

    public void release() {
        ClientCoreSDK.getInstance().release();
        this.resetInitFlag();
    }

    public void resetInitFlag() {
        this.init = false;
    }

    public ChatTransDataEventImpl getTransDataListener() {
        return this.transDataListener;
    }

    public ChatBaseEventImpl getBaseEventListener() {
        return this.baseEventListener;
    }

    public MessageQoSEventImpl getMessageQoSListener() {
        return this.messageQoSListener;
    }
}

