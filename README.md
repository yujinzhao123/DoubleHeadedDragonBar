
## android seekbar
`(双头龙控件，喜欢的老铁来个双击，来个star)`

 双向选择的进度条,回调返回百分比值，气泡控件根据需求传入一个View即可。

****
# 样式
![图](https://github.com/yujinzhao123/DoubleHeadedDragonBar/blob/master/alf86-zl32e.gif)
*****

## 使用
Grade
```groovy
	allprojects {
		repositories {
			...
			maven { url 'https://www.jitpack.io' }
		}
	}
        
        dependencies {
	        implementation 'com.github.yujinzhao123:DoubleHeadedDragonBar:1.0.4'
	}
```         
Maven
```groovy
	<repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://www.jitpack.io</url>
		</repository>
	</repositories>
        
       <dependency>
	    <groupId>com.github.yujinzhao123</groupId>
	    <artifactId>DoubleHeadedDragonBar</artifactId>
	    <version>1.0.4</version>
	</dependency>
```       
        
### 属性
```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <declare-styleable name="DoubleHeadedDragonBar">
        <!--进度条按钮宽高-->
        <attr name="button_width" format="dimension" />
        <attr name="button_height" format="dimension" />
        <!--进图条按钮图片-->
        <attr name="button_img" format="reference"/>
        <!--单位字体颜色-->
        <attr name="text_color" format="color"/>
        <!--进图条背景颜色-->
        <attr name="bg_color" format="color"/>
        <!--进度条颜色-->
        <attr name="value_color" format="color"/>
        <!--进度条宽-->
        <attr name="seek_height" format="dimension"/>
    </declare-styleable>

</resources>
```

### 案layout
```xml
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="#fff"
    tools:context=".MainActivity">
    <cn.bar.DoubleHeadedDragonBar
        android:layout_margin="10dp"
        android:id="@+id/bar"
        app:text_color="#5C6980"
        app:button_img="@drawable/button"
        android:layout_width="match_parent"
        android:layout_height="wrap_content" />

    <cn.bar.DoubleHeadedDragonBar
        android:layout_below="@+id/bar"
        android:layout_margin="10dp"
        android:id="@+id/bar1"
        app:text_color="#1B97F7"
        app:button_img="@mipmap/seek_button"
        app:button_height="40dp"
        app:button_width="40dp"
        app:bg_color="#999"
        app:value_color="#e97051"
        app:seek_height="6dp"
        android:layout_width="match_parent"
        android:layout_height="wrap_content" />
</RelativeLayout>
```
## Activity
```java
  DoubleHeadedDragonBar bar,bar1;
    TextView testView,testView1;

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
```
