package translatedemo.com.translatedemo.util;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import translatedemo.com.translatedemo.interfice.TextonClickLister;

public class CustomClickableSpan extends ClickableSpan {
    private Context mContext;
    private int mAtIndex;
    private TextonClickLister mlister;
    private int sindx = -1;
    public CustomClickableSpan(Context context, int atIndex,TextonClickLister mlister,int sindex) {
        mContext = context;
        mAtIndex = atIndex;
        this.sindx = sindex;
        this.mlister = mlister;
    }

    @Override
    public void onClick(@NonNull View widget) {
        if (widget instanceof TextView) {
            TextView textView = (TextView) widget;
            Log.d("SpanContent", "Content = " + textView.getText().toString());

            String str = textView.getText().toString();

            if(sindx<=str.length()) {
                String text = str.substring(mAtIndex, sindx);
                if (mlister != null) {
                    mlister.clickText(text);
                }
            }
//            Toast.makeText(mContext, , Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
//        ds.setColor(Color.BLUE);
        ds.setColor(Color.BLACK);
        ds.setUnderlineText(false);
        ds.clearShadowLayer();
    }


}
