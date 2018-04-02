package com.ingic.auditix.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created on 3/15/2018.
 */

public class NewsEpisodeEnt {

    @SerializedName("_index")
    private String _index;
    @SerializedName("_type")
    private String _type;
    @SerializedName("_id")
    private String _id;
    @SerializedName("title")
    private String title;
    @SerializedName("audio_link")
    private String audio_link;
    @SerializedName("audio_duration")
    private int audio_duration;
    @SerializedName("duration")
    private int duration;
    @SerializedName("image_url")
    private String image_url;
    @SerializedName("category")
    private String category;
    @SerializedName("timestamp")
    private String timestamp;
    @SerializedName("description")
    private String description;
    @SerializedName("source_image_url")
    private String source_image_url;
    @SerializedName("source_name")
    private String source_name;
    @SerializedName("source")
    private String source;
    @SerializedName("story_actionlog_data")
    private Story_actionlog_data story_actionlog_data;
    @SerializedName("link")
    private String link;
    @SerializedName("text_id")
    private String text_id;
    @SerializedName("tags")
    private String tags;
    @SerializedName("source_id")
    private String source_id;
    @SerializedName("channel_id")
    private String channel_id;
    @SerializedName("channel_name")
    private String channel_name;
    @SerializedName("source_text_id")
    private String source_text_id;
    @SerializedName("channel_text_id")
    private String channel_text_id;

    public String get_index() {
        return _index;
    }

    public void set_index(String _index) {
        this._index = _index;
    }

    public String get_type() {
        return _type;
    }

    public void set_type(String _type) {
        this._type = _type;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAudio_link() {
        return audio_link;
    }

    public void setAudio_link(String audio_link) {
        this.audio_link = audio_link;
    }

    public int getAudio_duration() {
        return audio_duration;
    }

    public void setAudio_duration(int audio_duration) {
        this.audio_duration = audio_duration;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSource_image_url() {
        return source_image_url;
    }

    public void setSource_image_url(String source_image_url) {
        this.source_image_url = source_image_url;
    }

    public String getSource_name() {
        return source_name;
    }

    public void setSource_name(String source_name) {
        this.source_name = source_name;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Story_actionlog_data getStory_actionlog_data() {
        return story_actionlog_data;
    }

    public void setStory_actionlog_data(Story_actionlog_data story_actionlog_data) {
        this.story_actionlog_data = story_actionlog_data;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getText_id() {
        return text_id;
    }

    public void setText_id(String text_id) {
        this.text_id = text_id;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getSource_id() {
        return source_id;
    }

    public void setSource_id(String source_id) {
        this.source_id = source_id;
    }

    public String getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(String channel_id) {
        this.channel_id = channel_id;
    }

    public String getChannel_name() {
        return channel_name;
    }

    public void setChannel_name(String channel_name) {
        this.channel_name = channel_name;
    }

    public String getSource_text_id() {
        return source_text_id;
    }

    public void setSource_text_id(String source_text_id) {
        this.source_text_id = source_text_id;
    }

    public String getChannel_text_id() {
        return channel_text_id;
    }

    public void setChannel_text_id(String channel_text_id) {
        this.channel_text_id = channel_text_id;
    }

    public static class Story_actionlog_data {
        @SerializedName("_index")
        private String _index;
        @SerializedName("_type")
        private String _type;
        @SerializedName("_id")
        private String _id;

        public String get_index() {
            return _index;
        }

        public void set_index(String _index) {
            this._index = _index;
        }

        public String get_type() {
            return _type;
        }

        public void set_type(String _type) {
            this._type = _type;
        }

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }
    }
}
