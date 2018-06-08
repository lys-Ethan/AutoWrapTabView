package com.pmlee.autowraptabview.bean;

import java.io.Serializable;

/**
 * Created by liyunshuang on 2018/4/23.
 * <p>
 * Email 522940943@qq.com or liyunshuang21@gmail.com
 */
public class Hobby implements Serializable {
    /**
     * 标签分类
     */
    private int category;
    /**
     * 标签ID
     */
    private int id;
    /**
     * 标签名
     */
    private String name;
    /**
     * 是否为共同兴趣
     */
    private boolean same;

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSame() {
        return same;
    }

    public void setSame(boolean same) {
        this.same = same;
    }
}

