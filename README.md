# tagview
simple tagview

아주아주 단순한 TagView

![tagview_sample](https://user-images.githubusercontent.com/5418274/74410330-30ceec80-4e7c-11ea-8796-beac89f3e19b.gif)


Add it to your build.gradle with:
```gradle
allprojects {
    repositories {
        maven { url "https://jitpack.io" }
    }
}
```
and:

```gradle
dependencies {
    implementation 'com.github.AndroidJustSeries:tagview:{latest version}'
}
```

## How to use
#XML
```
    <com.kds.just.tagview.TagLayout
        android:id="@+id/taglayout_xml"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="10dp"
        android:background="#ededed"
        app:horizontalSpacing="10dp"
        app:verticalSpacing="10dp">
        <Button
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginLeft="10dp"
            android:text="Button 06" />

        <Button
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginLeft="10dp"
            android:background="#ff0000"
            android:text="Tag Button 07" />
    </com.kds.just.tagview.TagLayout>
```
