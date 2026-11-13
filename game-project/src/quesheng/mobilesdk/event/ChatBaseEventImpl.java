/*
 * Decompiled with CFR 0.152.
 */
package quesheng.mobilesdk.event;

import java.util.Observer;
import net.openmob.mobileimsdk.java.event.ChatBaseEvent;
import net.openmob.mobileimsdk.java.utils.Log;

public class ChatBaseEventImpl
implements ChatBaseEvent {
    private static final String TAG = ChatBaseEventImpl.class.getSimpleName();
    private Observer loginOkForLaunchObserver = null;

    @Override
    public void onLoginMessage(int dwErrorCode) {
        if (dwErrorCode == 0) {
            Log.p(TAG, "\u3010DEBUG_UI\u3011IM\u670d\u52a1\u5668\u767b\u5f55/\u8fde\u63a5\u6210\u529f\uff01");
        } else {
            Log.e(TAG, "\u3010DEBUG_UI\u3011IM\u670d\u52a1\u5668\u767b\u5f55/\u8fde\u63a5\u5931\u8d25\uff0c\u9519\u8bef\u4ee3\u7801\uff1a" + dwErrorCode);
        }
        if (this.loginOkForLaunchObserver != null) {
            this.loginOkForLaunchObserver.update(null, dwErrorCode);
            this.loginOkForLaunchObserver = null;
        }
    }

    @Override
    public void onLinkCloseMessage(int dwErrorCode) {
        Log.e(TAG, "\u3010DEBUG_UI\u3011\u4e0eIM\u670d\u52a1\u5668\u7684\u7f51\u7edc\u8fde\u63a5\u51fa\u9519\u5173\u95ed\u4e86\uff0cerror\uff1a" + dwErrorCode);
    }

    public void setLoginOkForLaunchObserver(Observer loginOkForLaunchObserver) {
        this.loginOkForLaunchObserver = loginOkForLaunchObserver;
    }
}

