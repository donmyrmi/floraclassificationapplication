package com.sn.floraclassificationapplication.flowerdatabase;

/**
 * Created by Sapir on 19-Dec-15.
 */
public class AbstractFlower_Repo {

    protected final int HU_SET_MAX = 1;
    protected final int HU_SET_MIN = 0;
    protected final int HU_WEIGHT = 2;
    protected final int COLORS = 3;
    protected final int NUM_OF_MOMENTS = 8;
    protected final int NUM_OF_COLORS = 3;

    protected static FlowerGeneralAtt_Repo flowerGeneralAtt_repo;
    protected static FlowerLocation_Repo flowerLocation_repo;
    protected static FlowerAttributes_Repo flowerAttributes_repo;
}
