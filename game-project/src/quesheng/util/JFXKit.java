/*
 * Decompiled with CFR 0.152.
 */
package quesheng.util;

import javafx.scene.Node;

public final class JFXKit {
    public static double getScreenX(Node control) {
        control.applyCss();
        control.getParent().applyCss();
        control.getParent().layout();
        return control.getScene().getWindow().getX() + control.getScene().getX() + control.localToScene(0.0, 0.0).getX();
    }

    public static double getScreenY(Node control) {
        control.applyCss();
        control.getParent().applyCss();
        control.getParent().layout();
        return control.getScene().getWindow().getY() + control.getScene().getY() + control.localToScene(0.0, 0.0).getY();
    }
}

