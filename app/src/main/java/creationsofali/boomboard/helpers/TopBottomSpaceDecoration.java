package creationsofali.boomboard.helpers;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by ali on 4/21/17.
 */

public class TopBottomSpaceDecoration extends RecyclerView.ItemDecoration {

    private int bottomSpace, topSpace;

    public TopBottomSpaceDecoration(int bottomSpace, int topSpace) {
        this.bottomSpace = bottomSpace;
        this.topSpace = topSpace;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        // bottomPadding
        if (parent.getChildAdapterPosition(view) == parent.getAdapter().getItemCount() - 1) {
            outRect.bottom = bottomSpace;
            outRect.top = 0;
        }
        // topPadding
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.bottom = 0;
            outRect.top = topSpace;
        }

    }
}
