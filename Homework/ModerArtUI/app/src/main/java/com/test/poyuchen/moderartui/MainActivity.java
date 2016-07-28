package com.test.poyuchen.moderartui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.SeekBar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.app.AlertDialog;
import android.net.Uri;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private final static String url = "http://www.moma.org";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final TextView block1 = (TextView) findViewById(R.id.block1);
        final TextView block2 = (TextView) findViewById(R.id.block2);
        final TextView block3 = (TextView) findViewById(R.id.block3);
        final TextView block4 = (TextView) findViewById(R.id.block4);
        final TextView block5 = (TextView) findViewById(R.id.block5);

        final int oriColor1 = ((ColorDrawable) block1.getBackground()).getColor();
        final int oriColor2 = ((ColorDrawable) block2.getBackground()).getColor();
        final int oriColor3 = ((ColorDrawable) block3.getBackground()).getColor();
        final int oriColor4 = ((ColorDrawable) block4.getBackground()).getColor();
        final int oriColor5 = ((ColorDrawable) block5.getBackground()).getColor();

        SeekBar seekBar = (SeekBar)findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Auto-generated method stub
                setBackgroundColor(block1, oriColor1, progress);
                setBackgroundColor(block2, oriColor2, progress);
                setBackgroundColor(block3, oriColor3, progress);
                setBackgroundColor(block4, oriColor4, progress);
                setBackgroundColor(block5, oriColor5, progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Auto-generated method stub
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Auto-generated method stub
            }

            private void setBackgroundColor(TextView block, int OriginalColor, int progress) {
                float[] hsvColor = new float[3];
                Color.colorToHSV(OriginalColor, hsvColor);
                hsvColor[0] = hsvColor[0] + progress;
                hsvColor[0] = hsvColor[0] % 360;
                block.setBackgroundColor(Color.HSVToColor(Color.alpha(OriginalColor), hsvColor));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_MoreInformation) {
            showAlertDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showAlertDialog() {
        // Creating the AlertDialog with a custom xml layout (you can still use the default Android version)
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.alert_dialog, null);
        builder.setView(view);

        final AlertDialog dialog = builder.create();
        dialog.show();

        final Button positive = (Button) view.findViewById(R.id.positiveBtn);
        positive.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Maintain a count of user presses
                // Display count as text on the Button
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
            }
        });

        final Button negative = (Button) view.findViewById(R.id.negativeBtn);
        negative.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Maintain a count of user presses
                // Display count as text on the Button
                dialog.cancel();
            }
        });
    }
}
