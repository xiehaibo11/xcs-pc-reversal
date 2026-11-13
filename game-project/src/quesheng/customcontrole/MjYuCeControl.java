/*
 * Decompiled with CFR 0.152.
 */
package quesheng.customcontrole;

import javafx.beans.property.ObjectProperty;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import quesheng.customcontrole.MjControlInterface;

public final class MjYuCeControl
extends AnchorPane
implements MjControlInterface {
    private ImageView image = new ImageView();

    public MjYuCeControl() {
        this.getChildren().addAll((Node[])new Node[]{this.image});
    }

    @Override
    public final void clear() {
        this.getChildren().clear();
    }

    @Override
    public final void setImage(Image value) {
        this.imageProperty().set(value);
    }

    @Override
    public final Image getImage() {
        return this.image.getImage();
    }

    @Override
    public final ImageView getImageView() {
        return this.image;
    }

    @Override
    public final void addBottom(Node value) {
        this.getChildren().add(value);
    }

    public final ObjectProperty<Image> imageProperty() {
        return this.image.imageProperty();
    }

    @Override
    public final void addClass(String value) {
        this.getStyleClass().add(value);
    }

    @Override
    public final boolean hasClass(String value) {
        return this.getStyleClass().contains(value);
    }

    @Override
    public final void removeClass(String value) {
        this.getStyleClass().remove(value);
    }
}

