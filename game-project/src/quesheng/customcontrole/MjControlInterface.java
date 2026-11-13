/*
 * Decompiled with CFR 0.152.
 */
package quesheng.customcontrole;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public interface MjControlInterface {
    public void clear();

    public void setImage(Image var1);

    public Image getImage();

    public ImageView getImageView();

    public void addBottom(Node var1);

    public void addClass(String var1);

    public boolean hasClass(String var1);

    public void removeClass(String var1);
}

