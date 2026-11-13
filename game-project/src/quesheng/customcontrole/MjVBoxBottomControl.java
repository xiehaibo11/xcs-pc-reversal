/*
 * Decompiled with CFR 0.152.
 */
package quesheng.customcontrole;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import quesheng.customcontrole.MjControlInterface;

public final class MjVBoxBottomControl
extends VBox
implements MjControlInterface {
    private ImageView image;
    private VBox image_father;
    private VBox bottom = new VBox();

    public MjVBoxBottomControl() {
        this.bottom.setPrefHeight(10.0);
        this.bottom.setAlignment(Pos.CENTER);
        this.image_father = new VBox();
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
        this.image.setImage(value);
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

