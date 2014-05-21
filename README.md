Android-Simple-Pull-to-Refresh
==============================

Using the style created by [Chris Banes](https://github.com/chrisbanes/ActionBar-PullToRefresh) and recently implemented in the newest Gmail app, this is the easiest-to-implement Pull-to-Refresh possible.

instructions for use:

  1: Add RefreshableListView.java to your own files source files.

  2: Add RefreshableListView custom view to your layout file(possibly replacing a regular ListView).

```xml
<path.to.your.RefreshableListView
  android:id="@+id/RefreshList"
  style="@style/Widget.PullToRefresh.ProgressBar.Horizontal.Center"
  android:layout_width="match_parent"
  android:layout_height="match_parent" >
</path.to.your.RefreshableListView>
```

  3: In the activity's onCreate method set the onListRefreshListener and the onListLoadMoreListener.

```java
List.setOnListRefreshListener(this);
List.setOnListLoadMoreListener(this);
```
  4: Treat the RefreshableListView as a regular list and set your adapter.

```java
ListAdapter adapter = new someAdapter<someType>(...);
List.setAdapter(adapter);
```
  5: (Optionally) You can adjust the threshold for drag length and the number of items from the bottom before a call to load more items;

```java
List.setDragLength(500);
List.setDistanceFromBottom(10);
```
  6: Make a call to `finishRefresh()` or `finishLoadMore()` to notify the list that the refresh or load is finished.

```java
List.finishRefresh();
List.finishLoadingMore();
````
  7: To interact with this list like the the stock ListView, simply access it. 

```java
List.getListView().setAdapter(adapter); //This also sets the adapter
```

To change the color of the loading bar, manipulate the PNG's
***Warning:* changing the width might lead to some issue**

I can safely assume that this works on all versions of android (unlike the original wutwut).

Created by Joe Dailey

This content is released under the [MIT License](http://opensource.org/licenses/MIT).

Official Sample:

[![Get it on Google Play](http://www.android.com/images/brand/get_it_on_play_logo_small.png)](https://play.google.com/store/apps/details?id=com.refreshable.list&hl=en)


Please feel free to submit pull requests!

**Please also feel free to put a link to your project that uses this project at the bottom of this README!**
