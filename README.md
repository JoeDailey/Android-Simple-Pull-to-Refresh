Android-Simple-Pull-to-Refresh
==============================

Using the style created by [Chris Banes](https://github.com/chrisbanes/ActionBar-PullToRefresh) and recently implemented in the newest Gmail app, this is the easiest-to-implement Pull-to-Refresh possible.

instructions: 

1. add RefreshableListView.java to your own files.

2. add RefreshableListView custom view to your layout file(possibly replacing a regular ListView).

3. in the activity's onCreate method set the onListRefreshListener and the onListLoadMoreListener.

4. treat the RefreshableListView as a regular list and set your adapter.

5. (Optionally) you can adjust the threshold for drag length and the number of items from the bottom to call to load more items;

6. make a call to "finishRefresh()" to notify the list that the refresh is finished.


Note: Using the style and drawables provided will yield easiest results.

Assumedly works on all versions of android.

Created by Joe Dailey

No License

Sample:
[![Get it on Google Play](http://www.android.com/images/brand/get_it_on_play_logo_small.png)](https://play.google.com/store/apps/details?id=com.refreshable.list&hl=en)
