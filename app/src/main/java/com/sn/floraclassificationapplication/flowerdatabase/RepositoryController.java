package com.sn.floraclassificationapplication.flowerdatabase;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Interface for all of the repository classes
 */
public interface RepositoryController {

    public int insert(AbstractDBFlower abstractFlowerAttributes);
    public void delete(int flower_Id);
    public void update(AbstractDBFlower abstractFlowerAttributes);
    public ArrayList<HashMap<String, Object>> getAttributesList();
    public AbstractDBFlower getAttributesById(int Id);
    public void setParams(AbstractDBFlower DBFlower, Cursor cursor);

}
