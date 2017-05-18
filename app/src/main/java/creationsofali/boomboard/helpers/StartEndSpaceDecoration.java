package creationsofali.boomboard.helpers;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by ali on 4/21/17.
 */

public class StartEndSpaceDecoration extends RecyclerView.ItemDecoration {

    private int space;

    public StartEndSpaceDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        // startPadding
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.left = space;
            outRect.right = 0;
        }

        // endPadding
        if (parent.getChildAdapterPosition(view) == parent.getAdapter().getItemCount() - 1) {
            outRect.left = 0;
            outRect.right = space;
        }
    }
}
