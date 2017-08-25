package com.example.ivan.marktask.RecyclerViewPackage.helper;

import android.support.v7.widget.RecyclerView;

/**
 * Created by Ivan on 08.08.2017.
 */

public interface ItemTouchHelperAdapter {
    boolean onItemMove(int fromPosition, int toPosition);
    void onItemDismiss(int position);
}
