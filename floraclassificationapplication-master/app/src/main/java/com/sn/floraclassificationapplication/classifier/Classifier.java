package com.sn.floraclassificationapplication.classifier;

import com.sn.floraclassificationapplication.Flower;

/**
 * Created by Nadav on 19-Nov-15.
 */
public interface Classifier {
    public int[] Classify(Flower f);

}
