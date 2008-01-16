package com.atlassian.theplugin.bamboo;

import com.atlassian.theplugin.bamboo.BambooPlan;

/**
 * Created by IntelliJ IDEA.
 * User: mwent
 * Date: 2008-01-16
 * Time: 09:00:49
 * To change this template use File | Settings | File Templates.
 */
public class BambooPlanInfo implements BambooPlan {
    private String name;
    private String key;

    public BambooPlanInfo(String name, String key) {
        this.name = name;
        this.key = key;
    }

    public String getPlanName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlanKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
