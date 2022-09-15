package com.homecomingday.controller;

import java.util.List;

public class ItemList extends org.json.simple.ItemList {
    private List<?> items;
    private int totalCount;

    /**
     * @param items
     * @param totalCount
     */
    public ItemList(List<?> items, int totalCount) {
        super();
        this.items = items;
        this.totalCount = totalCount;
    }
    /**
     * @return items
     */
    public List<?> getItems() {
        return items;
    }
    /**
     * @param items
     *            설정할 items
     */

    public void setItems(List<?> items) {
        this.items = items;
    }
    /**
     * @return totalCount
     */
    public int getTotalCount() {
        return totalCount;
    }
    /**
     * @param totalCount
     *            설정할 totalCount
     */

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

}
