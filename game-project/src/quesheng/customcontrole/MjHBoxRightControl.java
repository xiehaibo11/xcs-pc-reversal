/*
 * Decompiled with CFR 0.152.
 */
package quesheng.customcontrole;

import javafx.beans.property.ObjectProperty;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import quesheng.customcontrole.MjControlInterface;

public final class MjHBoxRightControl
extends HBox
implements MjControlInterface {
    private ImageView image;
    private HBox image_father;
    private HBox bottom = new HBox();

    public MjHBoxRightControl() {
        this.bottom.setPrefWidth(10.0);
        this.bottom.setAlignment(Pos.CENTER);
        this.image_father = new HBox();
        this.image = new ImageView();
        this.image_father.getChildren().add(this.image);
        this.getChildren().addAll((Node[])new Node[]{this.image_father, this.bottom});
    }

    @Override
    public final void clear() {
        this.bottom.getChildren().clear();
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
        this.bottom.getChildren().add(value);
    }

    public final ObjectProperty<Image> imageProperty() {
        return this.image.imageProperty();
    }

    @Override
    public final void addClass(String value) {
        this.image_father.getStyleClass().add(value);
    }

    @Override
    public final boolean hasClass(String value) {
        return this.image_father.getStyleClass().contains(value);
    }

    @Override
    public final void removeClass(String value) {
        this.image_father.getStyleClass().remove(value);
    }
}

