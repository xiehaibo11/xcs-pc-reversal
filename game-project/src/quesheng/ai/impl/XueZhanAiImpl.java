/*
 * Decompiled with CFR 0.152.
 */
package quesheng.ai.impl;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import quesheng.ai.AbstractAi;
import quesheng.controller.MainController;
import quesheng.model.Mj;
import quesheng.util.Config;

public class XueZhanAiImpl
extends AbstractAi {
    public XueZhanAiImpl(MainController mainController) {
        super(mainController);
    }

    @Override
    public Mj tuijian() {
        int weight = 0;
        Mj mj1 = null;
        List<Mj> yina = Config.yina.stream().filter(mj -> mj.getNapai_direction() == MainController.zhihuifangxiang && mj.getPaizhuantai() == 0).collect(Collectors.toList());
        for (Mj mj2 : yina) {
            int w = this.ComputerWeight(mj2);
            if (mj2.getIsque()) {
                w += 200;
            }
            if (w <= weight) continue;
            weight = w;
            mj1 = mj2;
        }
        return mj1;
    }

    private int ComputerWeight(Mj mj) {
        List<Mj> yina = Config.yina.stream().filter(mj1 -> mj1.getNapai_direction() == MainController.zhihuifangxiang && !mj1.equals(mj)).collect(Collectors.toList());
        Collections.sort(yina, new MainController.ComparatorMjResult());
        int wedit = 0;
        for (int i = 0; i < yina.size() - 1; ++i) {
            if (((Mj)yina.get(i)).getResult() == ((Mj)yina.get(i + 1)).getResult()) {
                wedit += this.sige;
                continue;
            }
            if (((Mj)yina.get(i)).getPaizhuantai() == 0 && ((Mj)yina.get(i + 1)).getPaizhuantai() == 0 && ((Mj)yina.get(i + 1)).getResult() - ((Mj)yina.get(i)).getResult() == 1 && ((Mj)yina.get(i + 1)).getResult() / 10 == ((Mj)yina.get(i)).getResult() / 10) {
                wedit += this.lianxupai;
                continue;
            }
            if (((Mj)yina.get(i)).getPaizhuantai() != 0 || ((Mj)yina.get(i + 1)).getPaizhuantai() != 0 || ((Mj)yina.get(i + 1)).getResult() - ((Mj)yina.get(i)).getResult() != 2 || ((Mj)yina.get(i + 1)).getResult() / 10 != ((Mj)yina.get(i)).getResult() / 10) continue;
            wedit += this.lianxupai / 2;
        }
        List<Mj> yuce = Config.yuce.stream().filter(mj1 -> mj1.getYuce_direction() == MainController.zhihuifangxiang).collect(Collectors.toList());
        for (int i = 0; i < 3 && i < yuce.size(); ++i) {
            int finalI = i;
            if (yina.stream().filter(mj1 -> mj1.getResult() == ((Mj)yuce.get(finalI)).getResult()).count() > 0L) {
                wedit += this.sige / 2;
                continue;
            }
            if (yina.stream().filter(mj1 -> mj1.getPaizhuantai() == 0 && mj1.getResult() / 10 == ((Mj)yuce.get(finalI)).getResult() / 10 && Math.abs(mj1.getResult() - ((Mj)yuce.get(finalI)).getResult()) == 1).count() > 0L) {
                wedit += this.lianxupai / 2;
                continue;
            }
            if (yina.stream().filter(mj1 -> mj1.getPaizhuantai() == 0 && mj1.getResult() / 10 == ((Mj)yuce.get(finalI)).getResult() / 10 && Math.abs(mj1.getResult() - ((Mj)yuce.get(finalI)).getResult()) == 2).count() <= 0L) continue;
            wedit += this.lianxupai / 4;
        }
        return wedit;
    }
}

