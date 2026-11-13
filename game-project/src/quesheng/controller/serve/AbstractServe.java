/*
 * Decompiled with CFR 0.152.
 */
package quesheng.controller.serve;

import io.reactivex.ObservableEmitter;
import quesheng.controller.MainController;
import quesheng.controller.serve.ServeInterface;
import quesheng.model.Mj;

public class AbstractServe
implements ServeInterface {
    public MainController mainController;

    public AbstractServe(MainController mainController) {
        this.mainController = mainController;
    }

    @Override
    public void hupaixuanze(ObservableEmitter emitter) {
    }

    @Override
    public void danzhangkegang(ObservableEmitter emitter) {
    }

    @Override
    public void caishengxuanze(ObservableEmitter emitter) {
    }

    @Override
    public void napai(ObservableEmitter emitter, Mj mj) {
    }
}

