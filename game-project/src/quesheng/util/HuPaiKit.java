/*
 * Decompiled with CFR 0.152.
 */
package quesheng.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import quesheng.controller.MainController;
import quesheng.model.Mj;
import quesheng.model.SystemConfig;
import quesheng.util.Config;

public class HuPaiKit {
    public static Set<String> kehu_list = new HashSet<String>(4000);

    public static boolean HuPaiPanDin(List<Mj> mahs, int que) {
        if (Config.systemConfig.rengyijiu != 1 && Config.systemConfig.wanjiapaishu >= 13 && mahs.size() < 14) {
            return false;
        }
        if (Config.systemConfig.queyimeng == 1) {
            for (Mj mj2 : mahs) {
                if (!mj2.getIsque() || mj2.getChupai_direction() != -1 || mj2.caisheng) continue;
                return false;
            }
        }
        List<Mj> pais = new LinkedList<Mj>(mahs);
        if (Config.systemConfig.pengpaihuozhezhongfabaidui == 1 && pais.stream().filter(mj -> mj.getPaizhuantai() == 1).count() < 1L && pais.stream().filter(mj -> mj.getPaizhuantai() == 0 && mj.getResult() == 35).count() < 2L && pais.stream().filter(mj -> mj.getPaizhuantai() == 0 && mj.getResult() == 36).count() < 2L && pais.stream().filter(mj -> mj.getPaizhuantai() == 0 && mj.getResult() == 37).count() < 2L) {
            return false;
        }
        Iterator<Mj> iterator = pais.iterator();
        while (iterator.hasNext()) {
            Mj item = (Mj)iterator.next();
            if (item.getPaizhuantai() != 1 && item.getPaizhuantai() != 2 && item.getPaizhuantai() != 3 && item.getPaizhuantai() != 4) continue;
            iterator.remove();
        }
        if (pais.size() % 3 != 2) {
            return false;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (Mj mj3 : pais) {
            if (mj3.getResult() / 10 == que && !mj3.caisheng) {
                return false;
            }
            stringBuilder.append(mj3.getZhiBiaoZHi());
            stringBuilder.append('_');
            stringBuilder.append(mj3.caisheng);
            stringBuilder.append(",");
        }
        if (kehu_list.contains(stringBuilder.toString())) {
            return true;
        }
        if (Config.systemConfig.bixuyou19zipaifengpairenyiyizhang == 1 && pais.stream().filter(mj -> (mj.getResult() % 10 == 1 || mj.getResult() % 10 == 9) && mj.getResult() < 30 || mj.getResult() > 30 && mj.getResult() < 38).count() < 1L) {
            return false;
        }
        boolean kehu = HuPaiKit.hupaipanduan(pais);
        if (Config.systemConfig.yaojiyitongdangzipai == 1 && !kehu) {
            kehu = HuPaiKit.yaojiyitongdangzipai(pais);
        }
        if (Config.systemConfig.yaojiyitongdangzipaifengpai == 1 && !kehu) {
            kehu = HuPaiKit.yaojiyitongdangzipaifengpai(pais);
        }
        if (kehu) {
            if (kehu_list.size() > 4000) {
                kehu_list.remove(0);
            }
            kehu_list.add(stringBuilder.toString());
        } else if (Config.systemConfig.lanpai == 1 && pais.size() == 14) {
            kehu = HuPaiKit.lanpai(pais);
        }
        return kehu;
    }

    private static boolean hupaipanduan(List<Mj> yushimjs) {
        List<Mj> pais = new ArrayList<Mj>(yushimjs);
        List<Mj> caisheng_list = pais.stream().filter(mj -> mj.caisheng).collect(Collectors.toList());
        if (caisheng_list.size() > 0) {
            ArrayList<Integer> jiangs;
            Iterator<Mj> iterator3 = pais.iterator();
            while (iterator3.hasNext()) {
                Mj item = (Mj)iterator3.next();
                if (!item.caisheng) continue;
                iterator3.remove();
            }
            Map<Integer, Integer> map = new HashMap<Integer, Integer>(pais.size());
            for (Mj obj : pais) {
                if (map.containsKey(obj.getResult())) {
                    map.put(obj.getResult(), (Integer)map.get(obj.getResult()) + 1);
                    continue;
                }
                map.put(obj.getResult(), 1);
            }
            List<Integer> result_count = new ArrayList(map.values());
            if (Config.systemConfig.erwubajiang == 1) {
                jiangs = new ArrayList<Integer>();
                if (caisheng_list.size() >= 2) {
                    jiangs.add(((Mj)caisheng_list.get(0)).getResult());
                }
                for (Map.Entry entry : map.entrySet()) {
                    if ((Integer)entry.getKey() >= 30 || (Integer)entry.getValue() < 1 || (Integer)entry.getKey() % 10 != 2 && (Integer)entry.getKey() % 10 != 5 && (Integer)entry.getKey() % 10 != 8 || (Integer)entry.getValue() >= 4) continue;
                    jiangs.add((Integer)entry.getKey());
                }
                if (jiangs.size() < 1) {
                    return false;
                }
                for (Integer n : jiangs) {
                    int caisheng_count = caisheng_list.size();
                    if (n.intValue() == ((Mj)caisheng_list.get(0)).getResult() && map.getOrDefault(n, 0) == 0) {
                        caisheng_count -= 2;
                    }
                    List<Mj> suanpai = new LinkedList<Mj>(pais);
                    Iterator<Mj> iterator1 = suanpai.iterator();
                    int shuliang = 0;
                    while (iterator1.hasNext()) {
                        Mj item = (Mj)iterator1.next();
                        if (map.getOrDefault(item.getResult(), 0) == 4) {
                            iterator1.remove();
                            continue;
                        }
                        if (shuliang >= 2 || item.getResult() != n.intValue()) continue;
                        iterator1.remove();
                        if (map.getOrDefault(n, 0) == 1) {
                            --caisheng_count;
                        }
                        shuliang = (short)(shuliang + 1);
                    }
                    if (result_count.stream().filter(r -> r == 2).count() * 2L == (long)(14 - caisheng_count * 2)) {
                        return true;
                    }
                    if (result_count.stream().filter(r -> r == 4).count() > 1L && result_count.stream().filter(r -> r == 2).count() * 2L + result_count.stream().filter(r -> r == 4).count() * 4L == (long)suanpai.size()) {
                        return true;
                    }
                    if (result_count.stream().filter(r -> r == 2).count() == 5L && result_count.stream().filter(r -> r == 4).count() == 1L) {
                        return true;
                    }
                    if (result_count.stream().filter(r -> r == 4).count() * 4L == (long)suanpai.size()) {
                        return true;
                    }
                    for (Map.Entry entry : map.entrySet()) {
                        switch ((Integer)entry.getValue()) {
                            case 1: {
                                if (!((Integer)entry.getKey()).equals(n) || (Integer)entry.getKey() <= 30) break;
                                caisheng_count -= 2;
                                break;
                            }
                            case 2: {
                                break;
                            }
                            case 3: {
                                break;
                            }
                        }
                    }
                    if (caisheng_count < 0 || (suanpai.size() + caisheng_count) % 3 != 0) continue;
                    Collections.sort(suanpai, new MainController.ComparatorMjResult());
                    int b = (suanpai.size() + caisheng_count) / 3;
                    int var14_55 = 0;
                    while (var14_55 < b && caisheng_count >= 0) {
                        if (suanpai.size() == 1 && caisheng_count == 2) {
                            return true;
                        }
                        if (suanpai.size() == 2 && caisheng_count == 1 && ((Mj)suanpai.get(0)).getResult() == ((Mj)suanpai.get(1)).getResult()) {
                            return true;
                        }
                        Mj mj2 = (Mj)suanpai.get(0);
                        if (map.getOrDefault(mj2.getResult(), 0) > 2 && mj2.getResult() != n.intValue() && mj2.getResult() == ((Mj)suanpai.get(1)).getResult() && mj2.getResult() == ((Mj)suanpai.get(2)).getResult()) {
                            suanpai.remove(mj2);
                            mj2 = (Mj)suanpai.get(0);
                            suanpai.remove(mj2);
                            mj2 = (Mj)suanpai.get(0);
                            suanpai.remove(mj2);
                            if (suanpai.size() == 0) {
                                return true;
                            }
                        } else {
                            suanpai.remove(mj2);
                            if (map.getOrDefault(mj2.getResult(), 0) == 2 && mj2.getResult() != n.intValue() && (map.getOrDefault(mj2.getResult() + 1, 0) != 2 || mj2.getResult() + 1 == n) && mj2.getResult() == ((Mj)suanpai.get(0)).getResult()) {
                                mj2 = (Mj)suanpai.get(0);
                                suanpai.remove(mj2);
                                --caisheng_count;
                                if (suanpai.size() == 0) {
                                    return true;
                                }
                            } else {
                                Mj item;
                                boolean has = false;
                                Iterator<Mj> iterator2 = suanpai.iterator();
                                while (iterator2.hasNext()) {
                                    item = (Mj)iterator2.next();
                                    if (mj2.getResult() + 1 != item.getResult() || mj2.getResult() / 10 != item.getResult() / 10) continue;
                                    iterator2.remove();
                                    has = true;
                                    break;
                                }
                                if (!has) {
                                    --caisheng_count;
                                }
                                has = false;
                                iterator2 = suanpai.iterator();
                                while (iterator2.hasNext()) {
                                    item = (Mj)iterator2.next();
                                    if (mj2.getResult() + 2 != item.getResult() || mj2.getResult() / 10 != item.getResult() / 10) continue;
                                    iterator2.remove();
                                    has = true;
                                    break;
                                }
                                if (!has) {
                                    --caisheng_count;
                                }
                                if (caisheng_count < 0 || (suanpai.size() + caisheng_count) % 3 != 0) break;
                                if (suanpai.size() == 0) {
                                    return true;
                                }
                            }
                        }
                        ++var14_55;
                    }
                    if (suanpai.size() != 0 || caisheng_count < 0) continue;
                    return true;
                }
            } else {
                Mj item;
                if (Config.systemConfig.xiaoqidui == 1 && 14L - result_count.stream().filter(r -> r == 2).count() * 2L - result_count.stream().filter(r -> r == 1).count() == (long)caisheng_list.size() && (long)caisheng_list.size() == result_count.stream().filter(r -> r == 1).count()) {
                    return true;
                }
                if (Config.systemConfig.xiaoqidui == 1 && 14L - result_count.stream().filter(r -> r == 2).count() * 2L - result_count.stream().filter(r -> r == 3).count() * 4L - result_count.stream().filter(r -> r == 1).count() == (long)caisheng_list.size() - result_count.stream().filter(r -> r == 3).count() && (long)caisheng_list.size() == result_count.stream().filter(r -> r == 1).count() + result_count.stream().filter(r -> r == 3).count()) {
                    return true;
                }
                if (pais.size() == 14) {
                    List<Mj> mjs = new ArrayList<Mj>(pais);
                    Collections.sort(mjs, new MainController.ComparatorMjResult());
                    int xiaoqidui_caisheng_count = caisheng_list.size();
                    for (Map.Entry sub_enty : map.entrySet()) {
                        switch ((Integer)sub_enty.getValue()) {
                            case 1: {
                                Iterator<Mj> iterator6 = mjs.iterator();
                                while (iterator6.hasNext()) {
                                    Mj item2 = (Mj)iterator6.next();
                                    if (((Integer)sub_enty.getKey()).intValue() != item2.getResult() || (Integer)sub_enty.getKey() / 10 != item2.getResult() / 10) continue;
                                    iterator6.remove();
                                }
                                --xiaoqidui_caisheng_count;
                                break;
                            }
                            case 2: {
                                Iterator<Mj> iterator5 = mjs.iterator();
                                while (iterator5.hasNext()) {
                                    Mj item3 = (Mj)iterator5.next();
                                    if (((Integer)sub_enty.getKey()).intValue() != item3.getResult() || (Integer)sub_enty.getKey() / 10 != item3.getResult() / 10) continue;
                                    iterator5.remove();
                                }
                                break;
                            }
                            case 3: {
                                Iterator<Mj> iterator4 = mjs.iterator();
                                while (iterator4.hasNext()) {
                                    item = (Mj)iterator4.next();
                                    if (((Integer)sub_enty.getKey()).intValue() != item.getResult() || (Integer)sub_enty.getKey() / 10 != item.getResult() / 10) continue;
                                    iterator4.remove();
                                }
                                --xiaoqidui_caisheng_count;
                                break;
                            }
                            case 4: {
                                Iterator<Mj> iterator2 = mjs.iterator();
                                while (iterator2.hasNext()) {
                                    Mj mj2 = (Mj)iterator2.next();
                                    if (((Integer)sub_enty.getKey()).intValue() != mj2.getResult() || (Integer)sub_enty.getKey() / 10 != mj2.getResult() / 10) continue;
                                    iterator2.remove();
                                }
                                break;
                            }
                        }
                    }
                    if (Config.systemConfig.xiaoqidui == 1 && mjs.size() == 0 && (xiaoqidui_caisheng_count == 0 || xiaoqidui_caisheng_count > 0 && xiaoqidui_caisheng_count % 2 == 0)) {
                        return true;
                    }
                }
                jiangs = new ArrayList();
                if (caisheng_list.size() >= 2) {
                    jiangs.add(((Mj)caisheng_list.get(0)).getResult());
                }
                for (Map.Entry entry : map.entrySet()) {
                    if ((Integer)entry.getValue() < 1 || (Integer)entry.getValue() >= 4) continue;
                    jiangs.add((Integer)entry.getKey());
                }
                for (Integer n : jiangs) {
                    Mj item2;
                    Iterator<Mj> iterator2;
                    Mj mj3;
                    int n2;
                    int caisheng_count = caisheng_list.size();
                    if (n.intValue() == ((Mj)caisheng_list.get(0)).getResult() && map.getOrDefault(n, 0) == 0) {
                        caisheng_count -= 2;
                    }
                    List<Mj> suanpai = new LinkedList<Mj>(pais);
                    Iterator<Mj> iterator1 = suanpai.iterator();
                    int shuliang = 0;
                    while (iterator1.hasNext()) {
                        item = (Mj)iterator1.next();
                        if (shuliang >= 2 || item.getResult() != n.intValue()) continue;
                        iterator1.remove();
                        if (map.getOrDefault(n, 0) == 1) {
                            --caisheng_count;
                        }
                        shuliang = (short)(shuliang + 1);
                    }
                    if (Config.systemConfig.dongnanxibeizhongfabaishunzi == 1) {
                        int j;
                        List<Mj> zipai = suanpai.stream().filter(mj -> mj.getResult() > 30).collect(Collectors.toList());
                        Collections.sort(zipai, new MainController.ComparatorMjResult());
                        n2 = zipai.size() / 3;
                        for (j = 0; j < n2; ++j) {
                            Mj mj32 = (Mj)zipai.get(0);
                            if (map.getOrDefault(mj32.getResult(), 0) > 2 && mj32.getResult() != n.intValue() && mj32.getResult() == ((Mj)zipai.get(1)).getResult() && mj32.getResult() == ((Mj)zipai.get(2)).getResult()) {
                                zipai.remove(mj32);
                                suanpai.remove(mj32);
                                mj32 = (Mj)zipai.get(0);
                                zipai.remove(mj32);
                                suanpai.remove(mj32);
                                mj32 = (Mj)zipai.get(0);
                                zipai.remove(mj32);
                                suanpai.remove(mj32);
                                if (zipai.size() != 0) continue;
                                break;
                            }
                            List<Mj> del_mjs = new ArrayList<Mj>();
                            zipai.remove(mj32);
                            del_mjs.add(mj32);
                            iterator1 = zipai.iterator();
                            while (iterator1.hasNext()) {
                                Mj item6 = (Mj)iterator1.next();
                                if ((mj32.getResult() >= item6.getResult() || mj32.getResult() >= 35 || item6.getResult() >= 35) && (mj32.getResult() >= item6.getResult() || mj32.getResult() < 35 || item6.getResult() < 35)) continue;
                                iterator1.remove();
                                del_mjs.add(item6);
                                mj32 = item6;
                                break;
                            }
                            while (iterator1.hasNext()) {
                                Mj item3 = (Mj)iterator1.next();
                                if ((mj32.getResult() >= item3.getResult() || mj32.getResult() >= 35 || item3.getResult() >= 35) && (mj32.getResult() >= item3.getResult() || mj32.getResult() < 35 || item3.getResult() < 35)) continue;
                                iterator1.remove();
                                del_mjs.add(item3);
                                break;
                            }
                            suanpai.removeAll(del_mjs);
                        }
                        zipai = suanpai.stream().filter(mj -> mj.getResult() > 30).collect(Collectors.toList());
                        Collections.sort(zipai, new MainController.ComparatorMjResult());
                        n2 = zipai.size() % 3 > 0 ? zipai.size() / 3 + 1 : zipai.size() / 3;
                        for (j = 0; j < n2; ++j) {
                            if (caisheng_count < 0) {
                                return false;
                            }
                            if (zipai.size() == 1) {
                                caisheng_count -= 2;
                                break;
                            }
                            if (zipai.size() == 2 && ((Mj)zipai.get(0)).getResult() == ((Mj)zipai.get(1)).getResult()) {
                                --caisheng_count;
                                break;
                            }
                            mj3 = (Mj)zipai.get(0);
                            zipai.remove(mj3);
                            if (map.getOrDefault(mj3.getResult(), 0) == 2 && mj3.getResult() != n.intValue() && mj3.getResult() == ((Mj)zipai.get(0)).getResult()) {
                                mj3 = (Mj)zipai.get(0);
                                zipai.remove(mj3);
                                --caisheng_count;
                                if (zipai.size() != 0) continue;
                                break;
                            }
                            boolean has = false;
                            iterator2 = suanpai.iterator();
                            while (iterator2.hasNext()) {
                                Mj item5 = (Mj)iterator2.next();
                                if ((mj3.getResult() >= item5.getResult() || mj3.getResult() >= 35 || item5.getResult() >= 35) && (mj3.getResult() >= item5.getResult() || mj3.getResult() < 35 || item5.getResult() < 35)) continue;
                                iterator2.remove();
                                has = true;
                                break;
                            }
                            if (!has) {
                                --caisheng_count;
                            }
                            has = false;
                            iterator2 = suanpai.iterator();
                            while (iterator2.hasNext()) {
                                item2 = (Mj)iterator2.next();
                                if ((mj3.getResult() >= item2.getResult() || mj3.getResult() >= 35 || item2.getResult() >= 35) && (mj3.getResult() >= item2.getResult() || mj3.getResult() < 35 || item2.getResult() < 35)) continue;
                                iterator2.remove();
                                has = true;
                                break;
                            }
                            if (!has) {
                                --caisheng_count;
                            }
                            if (caisheng_count < 0 || (suanpai.size() + caisheng_count) % 3 != 0 || suanpai.size() == 0) break;
                        }
                        List<Mj> zipai1 = suanpai.stream().filter(mj -> mj.getResult() > 30).collect(Collectors.toList());
                        suanpai.removeAll(zipai1);
                    } else {
                        for (Map.Entry entry : map.entrySet()) {
                            switch ((Integer)entry.getValue()) {
                                case 1: {
                                    if (((Integer)entry.getKey()).equals(n) || (Integer)entry.getKey() <= 30) break;
                                    caisheng_count -= 2;
                                    iterator1 = suanpai.iterator();
                                    while (iterator1.hasNext()) {
                                        Mj item7 = (Mj)iterator1.next();
                                        if (item7.getResult() != ((Integer)entry.getKey()).intValue()) continue;
                                        iterator1.remove();
                                    }
                                    break;
                                }
                                case 2: {
                                    if (((Integer)entry.getKey()).equals(n) || (Integer)entry.getKey() <= 30) break;
                                    --caisheng_count;
                                    iterator1 = suanpai.iterator();
                                    while (iterator1.hasNext()) {
                                        Mj item4 = (Mj)iterator1.next();
                                        if (item4.getResult() != ((Integer)entry.getKey()).intValue()) continue;
                                        iterator1.remove();
                                    }
                                    break;
                                }
                                case 3: {
                                    break;
                                }
                            }
                        }
                    }
                    if (caisheng_count < 0 || (suanpai.size() + caisheng_count) % 3 != 0) continue;
                    Collections.sort(suanpai, new MainController.ComparatorMjResult());
                    List<Mj> duoyu = new ArrayList<Mj>();
                    while (suanpai.size() > 0) {
                        Mj mj4 = (Mj)suanpai.get(0);
                        if (map.getOrDefault(mj4.getResult(), 0) == 3 && mj4.getResult() != n.intValue() && suanpai.size() > 2 && mj4.getResult() == ((Mj)suanpai.get(1)).getResult() && mj4.getResult() == ((Mj)suanpai.get(2)).getResult()) {
                            suanpai.remove(mj4);
                            Mj mj5 = (Mj)suanpai.get(0);
                            suanpai.remove(mj5);
                            Mj mj6 = (Mj)suanpai.get(0);
                            suanpai.remove(mj6);
                            continue;
                        }
                        if (map.getOrDefault(mj4.getResult(), 0) == 2 && map.getOrDefault(mj4.getResult() + 1, 0) < 2 && mj4.getResult() + 1 != n && mj4.getResult() != n.intValue() && suanpai.size() > 2 && mj4.getResult() == ((Mj)suanpai.get(1)).getResult()) {
                            suanpai.remove(mj4);
                            Mj mj7 = (Mj)suanpai.get(0);
                            suanpai.remove(mj7);
                            --caisheng_count;
                            continue;
                        }
                        suanpai.remove(mj4);
                        Mj dierge = null;
                        iterator1 = suanpai.iterator();
                        if (mj4.getResult() < 30) {
                            while (iterator1.hasNext()) {
                                Mj item8 = (Mj)iterator1.next();
                                if (mj4.getResult() + 1 != item8.getResult() || mj4.getResult() / 10 != item8.getResult() / 10) continue;
                                iterator1.remove();
                                dierge = item8;
                                break;
                            }
                        }
                        Mj disange = null;
                        iterator1 = suanpai.iterator();
                        if (mj4.getResult() < 30) {
                            while (iterator1.hasNext()) {
                                Mj item9 = (Mj)iterator1.next();
                                if (mj4.getResult() + 2 != item9.getResult() || mj4.getResult() / 10 != item9.getResult() / 10) continue;
                                iterator1.remove();
                                disange = item9;
                                if (dierge != null) break;
                                --caisheng_count;
                                break;
                            }
                        }
                        if (disange != null) continue;
                        duoyu.add(mj4);
                        if (dierge == null) continue;
                        duoyu.add(dierge);
                    }
                    suanpai.addAll(duoyu);
                    if (suanpai.size() == 0 && caisheng_count >= 0) {
                        return true;
                    }
                    n2 = (suanpai.size() + caisheng_count) / 3;
                    for (int j = 0; j < n2 && caisheng_count >= 0; ++j) {
                        if (suanpai.size() == 1 && caisheng_count == 2) {
                            return true;
                        }
                        if (suanpai.size() == 2 && caisheng_count == 1 && ((Mj)suanpai.get(0)).getResult() == ((Mj)suanpai.get(1)).getResult()) {
                            return true;
                        }
                        mj3 = (Mj)suanpai.get(0);
                        if (map.getOrDefault(mj3.getResult(), 0) > 2 && mj3.getResult() != n.intValue() && mj3.getResult() == ((Mj)suanpai.get(1)).getResult() && mj3.getResult() == ((Mj)suanpai.get(2)).getResult()) {
                            suanpai.remove(mj3);
                            mj3 = (Mj)suanpai.get(0);
                            suanpai.remove(mj3);
                            mj3 = (Mj)suanpai.get(0);
                            suanpai.remove(mj3);
                            if (suanpai.size() != 0) continue;
                            return true;
                        }
                        suanpai.remove(mj3);
                        if (map.getOrDefault(mj3.getResult(), 0) == 2 && mj3.getResult() != n.intValue()) {
                            if (mj3.getResult() + 1 != n && map.getOrDefault(mj3.getResult() + 1, 0) > 0) {
                                if ((mj3.getResult() + 2 == n || map.getOrDefault(mj3.getResult() + 2, 0) <= 0) && mj3.getResult() == ((Mj)suanpai.get(0)).getResult()) {
                                    mj3 = (Mj)suanpai.get(0);
                                    suanpai.remove(mj3);
                                    --caisheng_count;
                                    if (suanpai.size() != 0) continue;
                                    return true;
                                }
                            } else if (mj3.getResult() == ((Mj)suanpai.get(0)).getResult()) {
                                mj3 = (Mj)suanpai.get(0);
                                suanpai.remove(mj3);
                                --caisheng_count;
                                if (suanpai.size() != 0) continue;
                                return true;
                            }
                        }
                        if (mj3.getResult() > 30 && map.getOrDefault(mj3.getResult(), 0) < 2) {
                            return false;
                        }
                        boolean has = false;
                        iterator2 = suanpai.iterator();
                        while (iterator2.hasNext()) {
                            item2 = (Mj)iterator2.next();
                            if (mj3.getResult() + 1 != item2.getResult() || mj3.getResult() / 10 != item2.getResult() / 10) continue;
                            iterator2.remove();
                            has = true;
                            break;
                        }
                        if (!has) {
                            --caisheng_count;
                        }
                        has = false;
                        iterator2 = suanpai.iterator();
                        while (iterator2.hasNext()) {
                            item2 = (Mj)iterator2.next();
                            if (mj3.getResult() + 2 != item2.getResult() || mj3.getResult() / 10 != item2.getResult() / 10) continue;
                            iterator2.remove();
                            has = true;
                            break;
                        }
                        if (!has) {
                            --caisheng_count;
                        }
                        if (caisheng_count < 0 || (suanpai.size() + caisheng_count) % 3 != 0) break;
                        if (suanpai.size() != 0) continue;
                        return true;
                    }
                    if (suanpai.size() != 0 || caisheng_count < 0) continue;
                    return true;
                }
            }
            return false;
        }
        Map<Integer, Integer> map = new HashMap<Integer, Integer>(pais.size());
        for (Mj obj : pais) {
            if (map.containsKey(obj.getResult())) {
                map.put(obj.getResult(), (Integer)map.get(obj.getResult()) + 1);
                continue;
            }
            map.put(obj.getResult(), 1);
        }
        List<Integer> result_count = new ArrayList(map.values());
        if (Config.systemConfig.erwubajiang == 1) {
            for (Map.Entry sub : map.entrySet()) {
                List<Mj> suanpai = new LinkedList<Mj>(pais);
                HashMap<Integer, Integer> hashMap = new HashMap<Integer, Integer>(suanpai.size());
                if ((Integer)sub.getKey() >= 30 || (Integer)sub.getValue() < 2 || (Integer)sub.getKey() % 10 != 2 && (Integer)sub.getKey() % 10 != 5 && (Integer)sub.getKey() % 10 != 8) continue;
                if (Config.systemConfig.xiaoqidui == 1 && result_count.stream().filter(r -> r == 2).count() * 2L == 14L) {
                    return true;
                }
                if (result_count.stream().filter(r -> r == 4).count() > 1L && result_count.stream().filter(r -> r == 2).count() * 2L + result_count.stream().filter(r -> r == 4).count() * 4L == (long)suanpai.size()) {
                    return true;
                }
                if (result_count.stream().filter(r -> r == 2).count() == 5L && result_count.stream().filter(r -> r == 4).count() == 1L) {
                    return true;
                }
                if (result_count.stream().filter(r -> r == 4).count() * 4L == (long)suanpai.size()) {
                    return true;
                }
                Iterator<Mj> iterator = suanpai.iterator();
                int shuliang = 0;
                while (iterator.hasNext()) {
                    Mj item = (Mj)iterator.next();
                    if (item.getResult() == ((Integer)sub.getKey()).intValue() && shuliang < 2) {
                        ++shuliang;
                        iterator.remove();
                    }
                    if (item.getResult() == ((Integer)sub.getKey()).intValue() || map.getOrDefault(item.getResult(), 0) != 4) continue;
                    if (!hashMap.containsKey(item.getResult())) {
                        hashMap.put(item.getResult(), 0);
                    }
                    if ((Integer)hashMap.get(item.getResult()) >= 3) continue;
                    iterator.remove();
                    hashMap.put(item.getResult(), (Integer)hashMap.get(item.getResult()) + 1);
                }
                if (suanpai.size() % 3 != 0) {
                    return false;
                }
                Collections.sort(suanpai, new MainController.ComparatorMjResult());
                int b = suanpai.size() / 3;
                for (int j = 0; j < b; ++j) {
                    Mj item;
                    Mj mj5 = (Mj)suanpai.get(0);
                    if (map.getOrDefault(mj5.getResult(), 0) == 3 && mj5.getResult() != ((Integer)sub.getKey()).intValue() && mj5.getResult() == ((Mj)suanpai.get(1)).getResult() && mj5.getResult() == ((Mj)suanpai.get(2)).getResult()) {
                        suanpai.remove(mj5);
                        mj5 = (Mj)suanpai.get(0);
                        suanpai.remove(mj5);
                        mj5 = (Mj)suanpai.get(0);
                        suanpai.remove(mj5);
                        continue;
                    }
                    suanpai.remove(mj5);
                    Iterator<Mj> iterator2 = suanpai.iterator();
                    while (iterator2.hasNext()) {
                        item = (Mj)iterator2.next();
                        if (mj5.getResult() + 1 != item.getResult() || mj5.getResult() / 10 != item.getResult() / 10) continue;
                        iterator2.remove();
                        break;
                    }
                    while (iterator2.hasNext()) {
                        item = (Mj)iterator2.next();
                        if (mj5.getResult() + 2 != item.getResult() || mj5.getResult() / 10 != item.getResult() / 10) continue;
                        iterator2.remove();
                        break;
                    }
                    if (suanpai.size() % 3 != 0) break;
                }
                if (suanpai.size() != 0) continue;
                return true;
            }
        } else {
            if (result_count.stream().filter(r -> r == 2).count() * 2L == (long)(Config.systemConfig.wanjiapaishu + 1)) {
                return true;
            }
            if (pais.size() == 2) {
                return ((Mj)pais.get(0)).getResult() == ((Mj)pais.get(1)).getResult();
            }
            map = new HashMap(pais.size());
            for (Mj obj : pais) {
                if (map.containsKey(obj.getResult())) {
                    map.put(obj.getResult(), (Integer)map.get(obj.getResult()) + 1);
                    continue;
                }
                map.put(obj.getResult(), 1);
            }
            result_count.clear();
            result_count.addAll(map.values());
            if (result_count.stream().filter(r -> r == 4).count() > 1L && result_count.stream().filter(r -> r == 2).count() * 2L + result_count.stream().filter(r -> r == 4).count() * 4L == (long)pais.size()) {
                return true;
            }
            if (result_count.stream().filter(r -> r == 2).count() == 5L && result_count.stream().filter(r -> r == 4).count() == 1L) {
                return true;
            }
            if (result_count.stream().filter(r -> r == 2).count() == 1L && result_count.stream().filter(r -> r == 4).count() * 4L == (long)(pais.size() - 2)) {
                return true;
            }
            for (Map.Entry sub_enty : map.entrySet()) {
                boolean keyisange = true;
                if (Config.systemConfig.dongnanxibeizhongfabaishunzi == 0) {
                    if (Config.systemConfig.guangxi152 == 0) {
                        if ((Integer)sub_enty.getValue() == 4 && (Integer)sub_enty.getKey() > 30) {
                            return false;
                        }
                        if ((Integer)sub_enty.getValue() == 1 && (Integer)sub_enty.getKey() > 30) {
                            return false;
                        }
                    }
                    boolean bl = keyisange = (Integer)sub_enty.getValue() > 2 && (Integer)sub_enty.getKey() < 30;
                }
                if ((Integer)sub_enty.getValue() != 2 && !keyisange) continue;
                List<Mj> suanpai = new LinkedList<Mj>(pais);
                Map<Integer, Integer> geshu = new HashMap<Integer, Integer>(suanpai.size());
                Iterator<Mj> iterator = suanpai.iterator();
                int shuliang = 0;
                while (iterator.hasNext()) {
                    Mj item = (Mj)iterator.next();
                    if (item.getResult() == ((Integer)sub_enty.getKey()).intValue() && shuliang < 2) {
                        ++shuliang;
                        iterator.remove();
                    }
                    if (item.getResult() == ((Integer)sub_enty.getKey()).intValue() || map.getOrDefault(item.getResult(), 0) != 4) continue;
                    if (!geshu.containsKey(item.getResult())) {
                        geshu.put(item.getResult(), 0);
                    }
                    if ((Integer)geshu.get(item.getResult()) >= 3) continue;
                    iterator.remove();
                    geshu.put(item.getResult(), (Integer)geshu.get(item.getResult()) + 1);
                }
                if (suanpai.size() == 0) {
                    return true;
                }
                if (suanpai.size() % 3 != 0) continue;
                if (Config.systemConfig.dongnanxibeizhongfabaishunzi == 1) {
                    List<Mj> zipai = suanpai.stream().filter(mj -> mj.getResult() > 30 && mj.getResult() <= 37).collect(Collectors.toList());
                    Map<Integer, Integer> dongnaixibei_map = new HashMap();
                    zipai.stream().filter(mj1 -> mj1.getResult() >= 31 && mj1.getResult() <= 34).collect(Collectors.toList()).forEach(mj1 -> {
                        if (dongnaixibei_map.containsKey(mj1.getResult())) {
                            dongnaixibei_map.put(mj1.getResult(), (Integer)dongnaixibei_map.get(mj1.getResult()) + 1);
                        } else {
                            dongnaixibei_map.put(mj1.getResult(), 1);
                        }
                    });
                    Set set = dongnaixibei_map.keySet();
                    dongnaixibei_map.values().removeIf(value -> value == 3);
                    Map<Integer, Integer> sort_dongnaixibei_map = HuPaiKit.sortByValue(dongnaixibei_map);
                    Set set2 = sort_dongnaixibei_map.keySet();
                    List<Integer> list = new ArrayList(set2);
                    while (list.size() >= 3) {
                        int tichu = 0;
                        for (Integer k : list) {
                            if ((Integer)sort_dongnaixibei_map.get(k) == 2) {
                                if (tichu >= 3) break;
                                sort_dongnaixibei_map.put(k, (Integer)sort_dongnaixibei_map.get(k) - 1);
                                ++tichu;
                                continue;
                            }
                            if ((Integer)sort_dongnaixibei_map.get(k) != 1) continue;
                            if (tichu >= 3) break;
                            sort_dongnaixibei_map.remove(k);
                            ++tichu;
                        }
                        sort_dongnaixibei_map = HuPaiKit.sortByValue(sort_dongnaixibei_map);
                        Set set3 = sort_dongnaixibei_map.keySet();
                        list = new ArrayList(set3);
                    }
                    if (list.size() != 0) continue;
                    Map<Integer, Integer> zhongfabai_map = new HashMap();
                    zipai.stream().filter(mj1 -> mj1.getResult() >= 35 && mj1.getResult() <= 37).collect(Collectors.toList()).forEach(mj1 -> {
                        if (zhongfabai_map.containsKey(mj1.getResult())) {
                            zhongfabai_map.put(mj1.getResult(), (Integer)zhongfabai_map.get(mj1.getResult()) + 1);
                        } else {
                            zhongfabai_map.put(mj1.getResult(), 1);
                        }
                    });
                    Set set4 = zhongfabai_map.keySet();
                    zhongfabai_map.values().removeIf(value -> value == 3);
                    Map<Integer, Integer> sort_zhongfabai_map = HuPaiKit.sortByValue(zhongfabai_map);
                    Set set5 = sort_zhongfabai_map.keySet();
                    list = new ArrayList(set5);
                    while (list.size() >= 3) {
                        int tichu = 0;
                        for (Integer k : list) {
                            if ((Integer)sort_zhongfabai_map.get(k) == 3) {
                                sort_zhongfabai_map.remove(k);
                                continue;
                            }
                            if ((Integer)sort_zhongfabai_map.get(k) == 2) {
                                if (tichu >= 3) break;
                                sort_zhongfabai_map.put(k, (Integer)sort_zhongfabai_map.get(k) - 1);
                                ++tichu;
                                continue;
                            }
                            if ((Integer)sort_zhongfabai_map.get(k) != 1) continue;
                            if (tichu >= 3) break;
                            sort_zhongfabai_map.remove(k);
                            ++tichu;
                        }
                        sort_zhongfabai_map = HuPaiKit.sortByValue(sort_zhongfabai_map);
                        Set set6 = sort_zhongfabai_map.keySet();
                        list = new ArrayList(set6);
                    }
                    if (list.size() != 0) continue;
                    List<Mj> zipai1 = suanpai.stream().filter(mj -> mj.getResult() > 30 && mj.getResult() <= 37).collect(Collectors.toList());
                    suanpai.removeAll(zipai1);
                }
                Collections.sort(suanpai, new MainController.ComparatorMjResult());
                List<Mj> duoyu = new ArrayList<Mj>();
                while (suanpai.size() > 0) {
                    Mj mj6 = (Mj)suanpai.get(0);
                    if (map.getOrDefault(mj6.getResult(), 0) == 3 && mj6.getResult() != ((Integer)sub_enty.getKey()).intValue() && suanpai.size() > 2 && mj6.getResult() == ((Mj)suanpai.get(1)).getResult() && mj6.getResult() == ((Mj)suanpai.get(2)).getResult()) {
                        suanpai.remove(mj6);
                        mj6 = (Mj)suanpai.get(0);
                        suanpai.remove(mj6);
                        mj6 = (Mj)suanpai.get(0);
                        suanpai.remove(mj6);
                        continue;
                    }
                    suanpai.remove(mj6);
                    Mj var14_75 = null;
                    Iterator<Mj> iterator1 = suanpai.iterator();
                    if (mj6.getResult() < 30) {
                        while (iterator1.hasNext()) {
                            Mj item = (Mj)iterator1.next();
                            if (mj6.getResult() + 1 != item.getResult() || mj6.getResult() / 10 != item.getResult() / 10) continue;
                            iterator1.remove();
                            var14_75 = item;
                            break;
                        }
                    }
                    if (var14_75 == null) {
                        duoyu.add(mj6);
                        continue;
                    }
                    Mj disange = null;
                    if (mj6.getResult() < 30) {
                        while (iterator1.hasNext()) {
                            Mj item = (Mj)iterator1.next();
                            if (mj6.getResult() + 2 != item.getResult() || mj6.getResult() / 10 != item.getResult() / 10) continue;
                            iterator1.remove();
                            disange = item;
                            break;
                        }
                    }
                    if (disange != null) continue;
                    duoyu.add(mj6);
                    duoyu.add(var14_75);
                }
                suanpai.addAll(duoyu);
                if (Config.systemConfig.guangxi152 == 1) {
                    List<Mj> dayu50 = suanpai.stream().filter(mj -> mj.getResult() > 50).collect(Collectors.toList());
                    List<Mj> zong = suanpai.stream().filter(mj1 -> mj1.getResult() == 55).collect(Collectors.toList());
                    List<Mj> he = suanpai.stream().filter(mj1 -> mj1.getResult() == 54).collect(Collectors.toList());
                    if (dayu50.size() > 0) {
                        dayu50.forEach(mj -> {
                            switch (mj.getResult()) {
                                case 52: {
                                    for (int i = 35; i <= 37; ++i) {
                                        int finalI = i;
                                        List<Mj> zhongfabai = suanpai.stream().filter(mj1 -> mj1.getResult() == finalI).collect(Collectors.toList());
                                        if (zhongfabai.size() == 2) {
                                            suanpai.removeAll(zhongfabai);
                                            suanpai.remove(mj);
                                        }
                                        if (zhongfabai.size() != 1 || zong.size() != 1) continue;
                                        suanpai.removeAll(zhongfabai);
                                        suanpai.removeAll(zong);
                                        zong.clear();
                                        suanpai.remove(mj);
                                    }
                                    break;
                                }
                                case 53: {
                                    for (int i = 31; i <= 34; ++i) {
                                        int finalI = i;
                                        List<Mj> zhongfabai = suanpai.stream().filter(mj1 -> mj1.getResult() == finalI).collect(Collectors.toList());
                                        if (zhongfabai.size() == 2) {
                                            suanpai.removeAll(zhongfabai);
                                            suanpai.remove(mj);
                                        }
                                        if (zhongfabai.size() != 1 || zong.size() != 1) continue;
                                        suanpai.removeAll(zhongfabai);
                                        suanpai.removeAll(zong);
                                        zong.clear();
                                        suanpai.remove(mj);
                                    }
                                    break;
                                }
                                case 56: {
                                    for (int i = 1; i <= 9; ++i) {
                                        int finalI = i;
                                        List<Mj> zhongfabai = suanpai.stream().filter(mj1 -> mj1.getResult() == finalI).collect(Collectors.toList());
                                        if (zhongfabai.size() != 2) continue;
                                        suanpai.removeAll(zhongfabai);
                                        suanpai.remove(mj);
                                    }
                                    break;
                                }
                                case 57: {
                                    for (int i = 21; i <= 29; ++i) {
                                        int finalI = i;
                                        List<Mj> zhongfabai = suanpai.stream().filter(mj1 -> mj1.getResult() == finalI).collect(Collectors.toList());
                                        if (zhongfabai.size() != 2) continue;
                                        suanpai.removeAll(zhongfabai);
                                        suanpai.remove(mj);
                                    }
                                    break;
                                }
                                case 58: {
                                    for (int i = 11; i <= 19; ++i) {
                                        int finalI = i;
                                        List<Mj> zhongfabai = suanpai.stream().filter(mj1 -> mj1.getResult() == finalI).collect(Collectors.toList());
                                        if (zhongfabai.size() != 2) continue;
                                        suanpai.removeAll(zhongfabai);
                                        suanpai.remove(mj);
                                    }
                                    break;
                                }
                                case 54: {
                                    for (int i = 1; i <= 29; ++i) {
                                        int finalI = i;
                                        List<Mj> zhongfabai = suanpai.stream().filter(mj1 -> mj1.getResult() == finalI).collect(Collectors.toList());
                                        if (zhongfabai.size() != 2 || he.size() != 1) continue;
                                        suanpai.removeAll(zhongfabai);
                                        suanpai.remove(mj);
                                        suanpai.removeAll(he);
                                        he.clear();
                                    }
                                    break;
                                }
                                case 55: {
                                    for (int i = 31; i <= 37; ++i) {
                                        int finalI = i;
                                        List<Mj> zhongfabai = suanpai.stream().filter(mj1 -> mj1.getResult() == finalI).collect(Collectors.toList());
                                        if (zhongfabai.size() != 2 || zong.size() != 1) continue;
                                        suanpai.removeAll(zhongfabai);
                                        suanpai.remove(mj);
                                        suanpai.removeAll(zong);
                                        zong.clear();
                                    }
                                    break;
                                }
                            }
                        });
                    }
                    Collections.sort(suanpai, new MainController.ComparatorMjResult());
                    while (suanpai.size() > 0) {
                        List<Mj> wan;
                        List<Mj> he1;
                        List<Mj> zong1;
                        Mj item;
                        Mj mj7 = (Mj)suanpai.get(0);
                        suanpai.remove(mj7);
                        Iterator<Mj> iterator1 = suanpai.iterator();
                        if (mj7.getResult() < 30) {
                            boolean has = false;
                            while (iterator1.hasNext()) {
                                item = (Mj)iterator1.next();
                                if (mj7.getResult() + 1 != item.getResult() || mj7.getResult() / 10 != item.getResult() / 10) continue;
                                iterator1.remove();
                                has = true;
                                break;
                            }
                            if (!has) {
                                zong1 = suanpai.stream().filter(mj1 -> mj1.getResult() == 55).collect(Collectors.toList());
                                he1 = suanpai.stream().filter(mj1 -> mj1.getResult() == 54).collect(Collectors.toList());
                                if (mj7.getResult() / 10 == 0) {
                                    wan = suanpai.stream().filter(mj1 -> mj1.getResult() == 56).collect(Collectors.toList());
                                    if (wan.size() > 0) {
                                        suanpai.remove(wan.get(0));
                                    } else if (he1.size() > 0) {
                                        suanpai.remove(he1.get(0));
                                    } else if (zong1.size() > 0) {
                                        suanpai.remove(zong1.get(0));
                                    }
                                }
                                if (mj7.getResult() / 10 == 1) {
                                    wan = suanpai.stream().filter(mj1 -> mj1.getResult() == 58).collect(Collectors.toList());
                                    if (wan.size() > 0) {
                                        suanpai.remove(wan.get(0));
                                    } else if (he1.size() > 0) {
                                        suanpai.remove(he1.get(0));
                                    } else if (zong1.size() > 0) {
                                        suanpai.remove(zong1.get(0));
                                    }
                                }
                                if (mj7.getResult() / 10 == 2) {
                                    wan = suanpai.stream().filter(mj1 -> mj1.getResult() == 57).collect(Collectors.toList());
                                    if (wan.size() > 0) {
                                        suanpai.remove(wan.get(0));
                                    } else if (he1.size() > 0) {
                                        suanpai.remove(he1.get(0));
                                    } else if (zong1.size() > 0) {
                                        suanpai.remove(zong1.get(0));
                                    }
                                }
                            }
                        }
                        if (mj7.getResult() < 30) {
                            boolean has = false;
                            iterator1 = suanpai.iterator();
                            while (iterator1.hasNext()) {
                                item = (Mj)iterator1.next();
                                if (mj7.getResult() + 2 != item.getResult() || mj7.getResult() / 10 != item.getResult() / 10) continue;
                                iterator1.remove();
                                has = true;
                                break;
                            }
                            if (!has) {
                                zong1 = suanpai.stream().filter(mj1 -> mj1.getResult() == 55).collect(Collectors.toList());
                                he1 = suanpai.stream().filter(mj1 -> mj1.getResult() == 54).collect(Collectors.toList());
                                if (mj7.getResult() / 10 == 0) {
                                    wan = suanpai.stream().filter(mj1 -> mj1.getResult() == 56).collect(Collectors.toList());
                                    if (wan.size() > 0) {
                                        suanpai.remove(wan.get(0));
                                    } else if (he1.size() > 0) {
                                        suanpai.remove(he1.get(0));
                                    } else if (zong1.size() > 0) {
                                        suanpai.remove(zong1.get(0));
                                    }
                                }
                                if (mj7.getResult() / 10 == 1) {
                                    wan = suanpai.stream().filter(mj1 -> mj1.getResult() == 58).collect(Collectors.toList());
                                    if (wan.size() > 0) {
                                        suanpai.remove(wan.get(0));
                                    } else if (he1.size() > 0) {
                                        suanpai.remove(he1.get(0));
                                    } else if (zong1.size() > 0) {
                                        suanpai.remove(zong1.get(0));
                                    }
                                }
                                if (mj7.getResult() / 10 == 2) {
                                    wan = suanpai.stream().filter(mj1 -> mj1.getResult() == 57).collect(Collectors.toList());
                                    if (wan.size() > 0) {
                                        suanpai.remove(wan.get(0));
                                    } else if (he1.size() > 0) {
                                        suanpai.remove(he1.get(0));
                                    } else if (zong1.size() > 0) {
                                        suanpai.remove(zong1.get(0));
                                    }
                                }
                            }
                        }
                        if (suanpai.size() % 3 == 0) continue;
                        break;
                    }
                }
                if (suanpai.size() != 0) continue;
                return true;
            }
        }
        return false;
    }

    private static boolean lanpai(List<Mj> yushimjs) {
        List<Mj> pais = new ArrayList<Mj>(yushimjs);
        List<Mj> caisheng_list = pais.stream().filter(mj -> mj.caisheng).collect(Collectors.toList());
        if (caisheng_list.size() > 0) {
            Iterator<Mj> iterator3 = pais.iterator();
            while (iterator3.hasNext()) {
                Mj item = (Mj)iterator3.next();
                if (!item.caisheng) continue;
                iterator3.remove();
            }
            Map<Integer, Integer> map = new HashMap<Integer, Integer>(pais.size());
            for (Mj obj : pais) {
                if (map.containsKey(obj.getResult())) {
                    map.put(obj.getResult(), (Integer)map.get(obj.getResult()) + 1);
                    if ((Integer)map.get(obj.getResult()) <= 1) continue;
                    return false;
                }
                map.put(obj.getResult(), 1);
            }
            LinkedList<Mj> linkedList = new LinkedList<Mj>(pais);
            Iterator<Mj> iterator1 = linkedList.iterator();
            while (iterator1.hasNext()) {
                Mj item = (Mj)iterator1.next();
                if (item.getResult() <= 30) continue;
                iterator1.remove();
            }
            if (linkedList.size() % 3 != 0) {
                return false;
            }
            Collections.sort(linkedList, new MainController.ComparatorMjResult());
            int caisheng_count = caisheng_list.size();
            int b = (linkedList.size() + caisheng_count) / 3;
            for (int j = 0; j < b; ++j) {
                Mj item;
                if (caisheng_count < 0) {
                    return false;
                }
                if (linkedList.size() == 1 && caisheng_count == 2) {
                    return true;
                }
                Mj mj2 = (Mj)linkedList.get(0);
                linkedList.remove(mj2);
                boolean has = false;
                Iterator<Mj> iterator2 = linkedList.iterator();
                while (iterator2.hasNext()) {
                    item = (Mj)iterator2.next();
                    if (item.getResult() - 3 < mj2.getResult() || mj2.getResult() / 10 != item.getResult() / 10) continue;
                    iterator2.remove();
                    has = true;
                    mj2 = item;
                    break;
                }
                if (!has) {
                    --caisheng_count;
                }
                has = false;
                iterator2 = linkedList.iterator();
                while (iterator2.hasNext()) {
                    item = (Mj)iterator2.next();
                    if (item.getResult() - 3 < mj2.getResult() || mj2.getResult() / 10 != item.getResult() / 10) continue;
                    iterator2.remove();
                    has = true;
                    break;
                }
                if (!has) {
                    --caisheng_count;
                }
                if (caisheng_count < 0) {
                    return false;
                }
                if ((linkedList.size() + caisheng_count) % 3 != 0) {
                    return false;
                }
                if (linkedList.size() != 0) continue;
                return true;
            }
            return linkedList.size() == 0 && caisheng_count > 0;
        }
        Map<Integer, Integer> map = new HashMap<Integer, Integer>(pais.size());
        for (Mj mj2 : pais) {
            if (map.containsKey(mj2.getResult())) {
                map.put(mj2.getResult(), (Integer)map.get(mj2.getResult()) + 1);
                if ((Integer)map.get(mj2.getResult()) <= 1) continue;
                return false;
            }
            map.put(mj2.getResult(), 1);
        }
        for (Map.Entry entry : map.entrySet()) {
            if ((Integer)entry.getValue() > 1) {
                return false;
            }
            List<Mj> suanpai = new LinkedList<Mj>(pais);
            Iterator<Mj> iterator2 = suanpai.iterator();
            while (iterator2.hasNext()) {
                Mj item = (Mj)iterator2.next();
                if (item.getResult() <= 30) continue;
                iterator2.remove();
            }
            if (suanpai.size() % 3 != 0) {
                return false;
            }
            Collections.sort(suanpai, new MainController.ComparatorMjResult());
            int b = suanpai.size() / 3;
            for (int j = 0; j < b; ++j) {
                Mj item;
                Mj mj3 = (Mj)suanpai.get(0);
                suanpai.remove(mj3);
                Iterator<Mj> iterator1 = suanpai.iterator();
                while (iterator1.hasNext()) {
                    item = (Mj)iterator1.next();
                    if (item.getResult() - 3 < mj3.getResult() || mj3.getResult() / 10 != item.getResult() / 10) continue;
                    iterator1.remove();
                    mj3 = item;
                    break;
                }
                while (iterator1.hasNext()) {
                    item = (Mj)iterator1.next();
                    if (item.getResult() - 3 < mj3.getResult() || mj3.getResult() / 10 != item.getResult() / 10) continue;
                    iterator1.remove();
                    break;
                }
                if (suanpai.size() % 3 == 0) continue;
                return false;
            }
            if (suanpai.size() != 0) continue;
            return true;
        }
        return false;
    }

    private static boolean yaojiyitongdangzipai(List<Mj> yushimjs) {
        List<Mj> pais = new ArrayList<Mj>(yushimjs);
        Map<Integer, Integer> map = new HashMap<Integer, Integer>(pais.size());
        for (Mj mj2 : pais) {
            if (map.containsKey(mj2.getResult())) {
                map.put(mj2.getResult(), (Integer)map.get(mj2.getResult()) + 1);
                continue;
            }
            map.put(mj2.getResult(), 1);
        }
        for (Map.Entry entry : map.entrySet()) {
            if ((Integer)entry.getValue() != 2 && (Integer)entry.getValue() <= 2) continue;
            List<Mj> suanpai = new LinkedList<Mj>(pais);
            Map<Integer, Integer> geshu = new HashMap<Integer, Integer>(suanpai.size());
            Iterator<Mj> iterator = suanpai.iterator();
            int shuliang = 0;
            while (iterator.hasNext()) {
                Mj item = (Mj)iterator.next();
                if (item.getResult() == ((Integer)entry.getKey()).intValue() && shuliang < 2) {
                    ++shuliang;
                    iterator.remove();
                }
                if (item.getResult() == ((Integer)entry.getKey()).intValue() || map.getOrDefault(item.getResult(), 0) < 3 || item.getResult() <= 34) continue;
                if (!geshu.containsKey(item.getResult())) {
                    geshu.put(item.getResult(), 0);
                }
                if ((Integer)geshu.get(item.getResult()) >= 3) continue;
                iterator.remove();
                geshu.put(item.getResult(), (Integer)geshu.get(item.getResult()) + 1);
            }
            if (suanpai.size() == 0) {
                return true;
            }
            if (suanpai.size() % 3 != 0) continue;
            int yichu = 35;
            int xuyao_yaoji_yitong = 0;
            while (suanpai.stream().filter(mj -> mj.getResult() > 34).count() > 0L || yichu != 35) {
                iterator = suanpai.iterator();
                boolean has = false;
                while (iterator.hasNext()) {
                    Mj item = (Mj)iterator.next();
                    if (item.getResult() != yichu) continue;
                    iterator.remove();
                    ++yichu;
                    has = true;
                    break;
                }
                if (!has) {
                    ++yichu;
                    ++xuyao_yaoji_yitong;
                }
                if (yichu <= 37) continue;
                yichu = 35;
            }
            long yaoji_yitong_count = suanpai.stream().filter(mj -> mj.getResult() == 11 || mj.getResult() == 21).count();
            if ((long)xuyao_yaoji_yitong > yaoji_yitong_count) continue;
            Collections.sort(suanpai, new MainController.ComparatorMjResult());
            Collections.reverse(suanpai);
            int daiding_yitiao_yitong = 0;
            int del_yaoji_yitong_count = 0;
            boolean has_3 = true;
            while (suanpai.size() > 0) {
                Mj item;
                Mj mj2 = (Mj)suanpai.get(0);
                if (mj2.getResult() == 11 || mj2.getResult() == 21) {
                    suanpai.remove(mj2);
                    ++del_yaoji_yitong_count;
                    continue;
                }
                if (map.getOrDefault(mj2.getResult(), 0) == 3 && mj2.getResult() != ((Integer)entry.getKey()).intValue() && mj2.getResult() == ((Mj)suanpai.get(1)).getResult() && mj2.getResult() == ((Mj)suanpai.get(2)).getResult()) {
                    suanpai.remove(mj2);
                    mj2 = (Mj)suanpai.get(0);
                    suanpai.remove(mj2);
                    mj2 = (Mj)suanpai.get(0);
                    suanpai.remove(mj2);
                    continue;
                }
                suanpai.remove(mj2);
                Iterator<Mj> iterator1 = suanpai.iterator();
                boolean has = false;
                while (iterator1.hasNext()) {
                    item = (Mj)iterator1.next();
                    if (mj2.getResult() - 1 != item.getResult() || mj2.getResult() / 10 != item.getResult() / 10) continue;
                    iterator1.remove();
                    has = true;
                    if (item.getResult() != 11 && item.getResult() != 21) break;
                    ++daiding_yitiao_yitong;
                    break;
                }
                if (!has) {
                    has_3 = false;
                    break;
                }
                has = false;
                while (iterator1.hasNext()) {
                    item = (Mj)iterator1.next();
                    if (mj2.getResult() - 2 != item.getResult() || mj2.getResult() / 10 != item.getResult() / 10) continue;
                    iterator1.remove();
                    has = true;
                    has_3 = true;
                    if (item.getResult() != 11 && item.getResult() != 21) break;
                    ++daiding_yitiao_yitong;
                    break;
                }
                if (has) continue;
                has_3 = false;
                break;
            }
            if (has_3 && suanpai.size() == 0 && (long)(xuyao_yaoji_yitong + daiding_yitiao_yitong) == yaoji_yitong_count) {
                return true;
            }
            if (!has_3 || suanpai.size() != 0 || (long)del_yaoji_yitong_count != yaoji_yitong_count || daiding_yitiao_yitong != 0 || xuyao_yaoji_yitong != 0) continue;
            return true;
        }
        return false;
    }

    private static boolean yaojiyitongdangzipaifengpai(List<Mj> yushimjs) {
        List<Mj> pais = new ArrayList<Mj>(yushimjs);
        Map<Integer, Integer> map = new HashMap<Integer, Integer>(pais.size());
        for (Mj mj2 : pais) {
            if (map.containsKey(mj2.getResult())) {
                map.put(mj2.getResult(), (Integer)map.get(mj2.getResult()) + 1);
                continue;
            }
            map.put(mj2.getResult(), 1);
        }
        for (Map.Entry entry : map.entrySet()) {
            if ((Integer)entry.getValue() != 2 && (Integer)entry.getValue() <= 2) continue;
            List<Mj> suanpai = new LinkedList<Mj>(pais);
            Map<Integer, Integer> geshu = new HashMap<Integer, Integer>(suanpai.size());
            Iterator<Mj> iterator = suanpai.iterator();
            int shuliang = 0;
            while (iterator.hasNext()) {
                Mj item = (Mj)iterator.next();
                if (item.getResult() == ((Integer)entry.getKey()).intValue() && shuliang < 2) {
                    ++shuliang;
                    iterator.remove();
                }
                if (item.getResult() == ((Integer)entry.getKey()).intValue() || map.getOrDefault(item.getResult(), 0) < 3 || item.getResult() <= 30) continue;
                if (!geshu.containsKey(item.getResult())) {
                    geshu.put(item.getResult(), 0);
                }
                if ((Integer)geshu.get(item.getResult()) >= 3) continue;
                iterator.remove();
                geshu.put(item.getResult(), (Integer)geshu.get(item.getResult()) + 1);
            }
            if (suanpai.size() == 0) {
                return true;
            }
            if (suanpai.size() % 3 != 0) continue;
            List<Mj> yiyoumajings = new ArrayList<Mj>();
            iterator = suanpai.iterator();
            while (iterator.hasNext()) {
                Mj item = (Mj)iterator.next();
                if (item.getResult() > 30 && !yiyoumajings.contains(item)) {
                    iterator.remove();
                    yiyoumajings.add(item);
                }
                if (yiyoumajings.size() != 3) continue;
                yiyoumajings.clear();
            }
            int xuyao_yaoji_yitong = 0;
            long feng_zi_count = suanpai.stream().filter(mj -> mj.getResult() > 30).count();
            if (feng_zi_count > 0L) {
                xuyao_yaoji_yitong = (int)(3L - feng_zi_count);
            }
            iterator = suanpai.iterator();
            while (iterator.hasNext()) {
                Mj item = (Mj)iterator.next();
                if (item.getResult() <= 30) continue;
                iterator.remove();
            }
            long yaoji_yitong_count = suanpai.stream().filter(mj -> mj.getResult() == 11 || mj.getResult() == 21).count();
            if ((long)xuyao_yaoji_yitong > yaoji_yitong_count) continue;
            Collections.sort(suanpai, new MainController.ComparatorMjResult());
            Collections.reverse(suanpai);
            int daiding_yitiao_yitong = 0;
            int del_yaoji_yitong_count = 0;
            boolean has_3 = true;
            while (suanpai.size() > 0) {
                Mj item;
                Mj mj2 = (Mj)suanpai.get(0);
                if (mj2.getResult() == 11 || mj2.getResult() == 21) {
                    suanpai.remove(mj2);
                    ++del_yaoji_yitong_count;
                    continue;
                }
                if (map.getOrDefault(mj2.getResult(), 0) == 3 && mj2.getResult() != ((Integer)entry.getKey()).intValue() && mj2.getResult() == ((Mj)suanpai.get(1)).getResult() && mj2.getResult() == ((Mj)suanpai.get(2)).getResult()) {
                    suanpai.remove(mj2);
                    mj2 = (Mj)suanpai.get(0);
                    suanpai.remove(mj2);
                    mj2 = (Mj)suanpai.get(0);
                    suanpai.remove(mj2);
                    continue;
                }
                suanpai.remove(mj2);
                Iterator<Mj> iterator1 = suanpai.iterator();
                boolean has = false;
                while (iterator1.hasNext()) {
                    item = (Mj)iterator1.next();
                    if (mj2.getResult() - 1 != item.getResult() || mj2.getResult() / 10 != item.getResult() / 10) continue;
                    iterator1.remove();
                    has = true;
                    if (item.getResult() != 11 && item.getResult() != 21) break;
                    ++daiding_yitiao_yitong;
                    break;
                }
                if (!has) {
                    has_3 = false;
                    break;
                }
                has = false;
                while (iterator1.hasNext()) {
                    item = (Mj)iterator1.next();
                    if (mj2.getResult() - 2 != item.getResult() || mj2.getResult() / 10 != item.getResult() / 10) continue;
                    iterator1.remove();
                    has = true;
                    has_3 = true;
                    if (item.getResult() != 11 && item.getResult() != 21) break;
                    ++daiding_yitiao_yitong;
                    break;
                }
                if (has) continue;
                has_3 = false;
                break;
            }
            if (has_3 && suanpai.size() == 0 && (long)(xuyao_yaoji_yitong + daiding_yitiao_yitong) == yaoji_yitong_count) {
                return true;
            }
            if (!has_3 || suanpai.size() != 0 || (long)del_yaoji_yitong_count != yaoji_yitong_count || daiding_yitiao_yitong != 0 || xuyao_yaoji_yitong != 0) continue;
            return true;
        }
        return false;
    }

    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        Map<K, V> result = new LinkedHashMap();
        map.entrySet().stream().sorted(Map.Entry.<K, V>comparingByValue().reversed()).forEachOrdered(e -> {
            result.put(e.getKey(), e.getValue());
        });
        return result;
    }

    public static void main(String[] args) {
        Config.systemConfig = new SystemConfig();
        Config.systemConfig.dongnanxibeizhongfabaishunzi = 1;
        List<Mj> mjs = new ArrayList<Mj>();
        Mj mj1 = new Mj();
        mj1.setResult(1);
        mj1.setPaizhuantai(4);
        mjs.add(mj1);
        Mj mj2 = new Mj();
        mj2.setResult(2);
        mj2.setPaizhuantai(4);
        mjs.add(mj2);
        Mj mj3 = new Mj();
        mj3.setResult(3);
        mj3.setPaizhuantai(4);
        mjs.add(mj3);
        Mj mj4 = new Mj();
        mj4.setResult(16);
        mjs.add(mj4);
        Mj mj5 = new Mj();
        mj5.setResult(16);
        mjs.add(mj5);
        Mj mj6 = new Mj();
        mj6.setResult(24);
        mjs.add(mj6);
        Mj mj7 = new Mj();
        mj7.setResult(25);
        mjs.add(mj7);
        Mj mj8 = new Mj();
        mj8.setResult(26);
        mjs.add(mj8);
        Mj mj9 = new Mj();
        mj9.setResult(31);
        mjs.add(mj9);
        Mj mj10 = new Mj();
        mj10.setResult(31);
        mjs.add(mj10);
        Mj mj11 = new Mj();
        mj11.setResult(31);
        mjs.add(mj11);
        Mj mj12 = new Mj();
        mj12.setResult(35);
        mjs.add(mj12);
        Mj mj13 = new Mj();
        mj13.setResult(36);
        mjs.add(mj13);
        Mj mj14 = new Mj();
        mj14.setResult(37);
        mjs.add(mj14);
        long xx = System.currentTimeMillis();
        System.out.println("+++++++++++++++++" + HuPaiKit.HuPaiPanDin(mjs, -1));
        System.out.println("+++++++++++++++++++++++++" + (System.currentTimeMillis() - xx));
    }
}

