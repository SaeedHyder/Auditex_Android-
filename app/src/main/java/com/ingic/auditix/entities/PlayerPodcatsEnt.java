package com.ingic.auditix.entities;

import java.util.ArrayList;

/**
 * Created on 7/13/18.
 */
public class PlayerPodcatsEnt {
    private PodcastDetailHomeEnt podcastDetailHomeEnt;
    private ArrayList<PodcastEpisodeEnt> podcastEpisodeEnts;

    public PlayerPodcatsEnt(PodcastDetailHomeEnt podcastDetailHomeEnt, ArrayList<PodcastEpisodeEnt> podcastEpisodeEnts) {
        this.podcastDetailHomeEnt = podcastDetailHomeEnt;
        this.podcastEpisodeEnts = podcastEpisodeEnts;
    }

    public PodcastDetailHomeEnt getPodcastDetail() {
        return podcastDetailHomeEnt;
    }

    public ArrayList<PodcastEpisodeEnt> getPodcastEpisodeList() {
        return podcastEpisodeEnts;
    }
}
