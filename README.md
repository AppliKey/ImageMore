#ImageMore

A simple UI element for a long list of images which are not put on screen

<img src="screenshots/demo.gif" alt="" width="240"/>

## Usage

Declare in your layout:

```xml
    <com.applikeysolutions.imagemore.ImageMoreView
        android:id="@+id/imageMore"
        android:layout_width="match_parent"
        android:layout_height="64dp"
        app:gravity="center"
        app:minItemSpacing="16dp" />
```

Then just add your items to adapter:

```java
        ImageMoreView<Adapter> imageMore = (ImageMoreView<Adapter>) findViewById(R.id.imageMore);
        Adapter adapter = new Adapter();
        adapter.update(items);
        imageMore.setAdapter(adapter);
```
If you want to show MoreIndicator, your adapter must implement interface ImageMoreAdapter.

```java
        class Adapter extends BaseAdapter implements ImageMoreAdapter
```

Also you can customize view programmatically:

```java
        imageMore.setMinItemSpacing(getResources().getDimensionPixelSize(R.dimen.item_spacing));
        imageMore.setGravity(Gravity.CENTER);
```

See [sample](sample/src/main/java/com/applikeysolutions/imagemore/example/ImageMoreExampleActivity.java).