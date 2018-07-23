package com.ingic.auditix.interfaces;

import com.ingic.auditix.entities.EnableFilterDataEnt;

/**
 * Created on 1/16/2018.
 */

public interface FilterDoneClickListener {
    void onDoneFiltering(EnableFilterDataEnt filters, boolean isClear);
}
