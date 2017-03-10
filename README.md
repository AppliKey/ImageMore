#ImageMore

A simple UI element for a long list of images which are not put on screen

<img src="screenshots/ImageMore.PNG" alt="" width="240"/>

## Usage

Declare in your layout:

```xml
        <com.applikeysolutions.imagemore.ImageMore
            android:id="@+id/imageMore"
            android:layout_width="match_parent"
            app:counterBackground="@drawable/round_counter"
            android:layout_height="40dp" />
```

Then just add links to images:

```java
        Image imageMore = (ImageMore) findViewById(R.id.imageMore);
        imageMore.addItem("https://images-na.ssl-images-amazon.com/images/I/7106mGW8G0L._CR0,204,1224,1224_UX128.jpg");
```

See [sample](sample/src/main/java/com/applikeysolutions/imagemore/example/ImageMoreExampleActivity.java).