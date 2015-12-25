package com.sn.floraclassificationapplication.classifier;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sn.floraclassificationapplication.R;
import com.sn.floraclassificationapplication.segmenter.ImageController;

import org.w3c.dom.Text;

/**
 * Created by Nadav on 19-Nov-15.
 */
public class ResultList extends ArrayAdapter<String> {
    private final AppCompatActivity context;
    private final String[] upTxt,downTxt;
    private final int[] imageId;
    private final String[] rankText;
    private final boolean[] isRGB;
    private ImageController ic = ImageController.getInstance();

    public ResultList(AppCompatActivity context,
                      String[] upTxt, int[] imageId, String[] downTxt, String[] rankText, boolean[] isRGB) {
        super(context, R.layout.resultsingle, upTxt);
        this.context = context;
        this.upTxt = upTxt;
        this.downTxt = downTxt;
        this.rankText = rankText;
        this.isRGB = isRGB;
        this.imageId = imageId;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.resultsingle, null, true);
        TextView upTxtTitle = (TextView) rowView.findViewById(R.id.uptxt);
        TextView downTxtTitle = (TextView) rowView.findViewById(R.id.downtxt);
        TextView rankTxtTitle = (TextView) rowView.findViewById(R.id.rankText);

        Bitmap tempBi;
        ImageView imageView = (ImageView) rowView.findViewById(R.id.img);
        if (isRGB[position]) {
            tempBi = ic.createImage(64,64,imageId[position]);
        } else {
            tempBi = BitmapFactory.decodeResource(context.getResources(), imageId[position]);
        }
        //tempBi = ic.getRoundedShape(tempBi);


        upTxtTitle.setText(upTxt[position]);
        downTxtTitle.setText(downTxt[position]);
        rankTxtTitle.setText(rankText[position]);
        imageView.setImageBitmap(tempBi);
        //imageView.setImageResource(imageId[position]);
        return rowView;
    }
}
