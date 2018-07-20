package com.ingic.auditix.entities;

import android.support.v7.widget.LinearLayoutCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 7/16/18.
 */
public class PlayerNewsEnt {
    private NewItemDetailEnt detailEnt;
    private ArrayList<NewsEpisodeEnt> newsepisodeslist;

    public PlayerNewsEnt(NewItemDetailEnt detailEnt, List<NewsEpisodeEnt> newsepisodeslist) {
        this.detailEnt = detailEnt;
        this.newsepisodeslist = new ArrayList<>(newsepisodeslist);
    }

    public NewItemDetailEnt getDetailEnt() {
        return detailEnt;
    }

    public ArrayList<NewsEpisodeEnt> getNewsepisodeslist() {
        return newsepisodeslist;
    }
}
