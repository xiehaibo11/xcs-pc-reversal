/*
 * Decompiled with CFR 0.152.
 */
package quesheng.mobilesdk.event;

import java.util.ArrayList;
import net.openmob.mobileimsdk.java.event.MessageQoSEvent;
import net.openmob.mobileimsdk.java.utils.Log;
import net.openmob.mobileimsdk.server.protocal.Protocal;

public class MessageQoSEventImpl
implements MessageQoSEvent {
    private static final String TAG = MessageQoSEventImpl.class.getSimpleName();

    @Override
    public void messagesLost(ArrayList<Protocal> lostMessages) {
        Log.d(TAG, "\u3010DEBUG_UI\u3011\u6536\u5230\u7cfb\u7edf\u7684\u672a\u5b9e\u65f6\u9001\u8fbe\u4e8b\u4ef6\u901a\u77e5\uff0c\u5f53\u524d\u5171\u6709" + lostMessages.size() + "\u4e2a\u5305QoS\u4fdd\u8bc1\u673a\u5236\u7ed3\u675f\uff0c\u5224\u5b9a\u4e3a\u3010\u65e0\u6cd5\u5b9e\u65f6\u9001\u8fbe\u3011\uff01");
    }

    @Override
    public void messagesBeReceived(String theFingerPrint) {
        if (theFingerPrint != null) {
            Log.d(TAG, "\u3010DEBUG_UI\u3011\u6536\u5230\u5bf9\u65b9\u5df2\u6536\u5230\u6d88\u606f\u4e8b\u4ef6\u7684\u901a\u77e5\uff0cfp=" + theFingerPrint);
        }
    }
}

