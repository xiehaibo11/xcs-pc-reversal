/*
 * Decompiled with CFR 0.152.
 */
package quesheng.customcontrole;

import javafx.beans.property.ObjectProperty;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import quesheng.customcontrole.MjControlInterface;
import quesheng.util.Config;

public final class MjVBoxTopControl
extends AnchorPane
implements MjControlInterface,
Cloneable {
    private ImageView image;
    private VBox image_father;
    private VBox bottom = new VBox();

    public MjVBoxTopControl() {
        this.bottom.setPrefHeight(10.0);
        this.bottom.setTranslateY(-3.0);
        this.bottom.setPrefWidth(Config.MJ_WIDTH);
        this.bottom.setAlignment(Pos.CENTER);
        this.image_father = new VBox();
        this.image = new ImageView();
        this.image_father.getChildren().add(this.image);
        this.getChildren().addAll((Node[])new Node[]{this.bottom, this.image_father});
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
        value.getParent().applyCss();
        value.getParent().layout();
        this.bottom.setTranslateY(this.bottom.getTranslateY() - value.getBoundsInLocal().getHeight());
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

