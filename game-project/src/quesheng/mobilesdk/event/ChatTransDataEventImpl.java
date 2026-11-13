/*
 * Decompiled with CFR 0.152.
 */
package quesheng.mobilesdk.event;

import net.openmob.mobileimsdk.java.event.ChatTransDataEvent;
import net.openmob.mobileimsdk.java.utils.Log;
import quesheng.controller.MainController;

public class ChatTransDataEventImpl
implements ChatTransDataEvent {
    private static final String TAG = ChatTransDataEventImpl.class.getSimpleName();
    private MainController mainGUI = null;

    @Override
    public void onTransBuffer(String fingerPrintOfProtocal, String userid, String dataContent, int typeu) {
        Log.d(TAG, "\u3010DEBUG_UI\u3011[typeu=" + typeu + "]\u6536\u5230\u6765\u81ea\u7528\u6237" + userid + "\u7684\u6d88\u606f:" + dataContent);
        if (this.mainGUI != null && typeu == 1) {
            this.mainGUI.jiance();
        }
    }

    public ChatTransDataEventImpl setMainGUI(MainController mainGUI) {
        this.mainGUI = mainGUI;
        return this;
    }

    @Override
    public void onErrorResponse(int errorCode, String errorMsg) {
        Log.d(TAG, "\u3010DEBUG_UI\u3011\u6536\u5230\u670d\u52a1\u7aef\u9519\u8bef\u6d88\u606f\uff0cerrorCode=" + errorCode + ", errorMsg=" + errorMsg);
        if (errorCode == 301) {
            System.out.println("\u670d\u52a1\u7aef\u4f1a\u8bdd\u5df2\u5931\u6548\uff0c\u81ea\u52a8\u767b\u9646/\u91cd\u8fde\u5c06\u542f\u52a8! (" + errorCode + ")");
        } else {
            System.out.println("Server\u53cd\u9988\u9519\u8bef\u7801\uff1a" + errorCode + ",errorMsg=" + errorMsg);
        }
    }
}

