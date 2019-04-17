package doubleheadeddragonbar.cn.doubleheadeddragonbar;


import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;

import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import cn.bar.DoubleHeadedDragonBar;

public class MainActivity extends Activity {


    DoubleHeadedDragonBar bar, bar1;
    TextView testView, testView1, testView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final int maxValue = 6;
        bar = findViewById(R.id.bar);
        //设置单位刻度显示
        bar.setUnit("0公斤", "6公斤");
        bar.setCallBack(new DoubleHeadedDragonBar.DhdBarCallBack() {

            @Override
            public String getMinMaxString(int value, int value1) {
                return value + "~" + value1;
            }


            @Override
            public void onEndTouch(float minPercentage, float maxPercentage) {

            }
        });
        //设置气泡按钮
        testView2 = (TextView) LayoutInflater.from(this).inflate(R.layout.toast_view, null);
        bar.setToastView2(testView2);

        testView = (TextView) LayoutInflater.from(this).inflate(R.layout.toast_view, null);
        bar.setToastView(testView);
        testView1 = (TextView) LayoutInflater.from(this).inflate(R.layout.toast_view, null);
        bar.setToastView1(testView1);
        bar.setMinValue(20);
        bar.setMaxValue(25);
//        bar.invalidate();



        bar1 = findViewById(R.id.bar1);
        bar1.setUnit("0", "100");
        bar1.setMinValue(10);
        bar1.setMaxValue(80);
    }

    public void popup(View view){
        // 用于PopupWindow的View
        View contentView=LayoutInflater.from(this).inflate(R.layout.pp_layout, null, false);

        PopupWindow window=new PopupWindow(contentView, 500, 500, true);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setOutsideTouchable(true);
        window.setTouchable(true);

        final DoubleHeadedDragonBar bar3 = contentView.findViewById(R.id.bar3);
        bar3.setUnit("0", "100");
        bar3.setMinValue(10);
        bar3.setMaxValue(80);
        TextView testView2 = (TextView) LayoutInflater.from(this).inflate(R.layout.toast_view, null);
        bar3.setToastView(testView2);
        TextView testView1 = (TextView) LayoutInflater.from(this).inflate(R.layout.toast_view, null);
        bar3.setToastView1(testView1);

        window.showAsDropDown(view);
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                bar3.close();
            }
        });
    }
}
