package com.qzb.http_java;

import java.io.Serializable;
import java.util.List;

public class DataBean implements Serializable {
    List<String> list;

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }
}
