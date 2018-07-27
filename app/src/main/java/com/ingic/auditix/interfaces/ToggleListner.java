package com.ingic.auditix.interfaces;

import com.ingic.auditix.entities.News;

public interface ToggleListner {

    void onCheckedChanged(Object entity, int position, boolean b, String channel);
}
