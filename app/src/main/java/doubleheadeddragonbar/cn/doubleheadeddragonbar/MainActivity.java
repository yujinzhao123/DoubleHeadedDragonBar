package doubleheadeddragonbar.cn.doubleheadeddragonbar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import cn.bar.DoubleHeadedDragonBar;

public class MainActivity extends AppCompatActivity {


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
        testView.setText("0");
        testView1 = (TextView) LayoutInflater.from(this).inflate(R.layout.toast_view, null);
        bar.setToastView1(testView1);
        testView1.setText("6");


        bar1 = findViewById(R.id.bar1);
        bar1.setUnit("0", "100");
        bar1.setMinValue(10);
        bar1.setMaxValue(80);
    }
}
