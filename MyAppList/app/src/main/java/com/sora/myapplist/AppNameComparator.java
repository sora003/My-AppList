package com.sora.myapplist;

import java.util.Comparator;

import static com.sora.myapplist.Cn2Spell.converterToFirstSpell;
import static com.sora.myapplist.Cn2Spell.converterToSpell;

/**
 * Created by Sora on 2015/12/13.
 */
public class AppNameComparator implements Comparator<AppInfo> {

    @Override
    public int compare(AppInfo a1,AppInfo a2) {
        String cn1 = converterToFirstSpell(a1.getAppName());
        String cn2 = converterToFirstSpell(a2.getAppName());
        return cn1.compareTo(cn2);
    }
}
s