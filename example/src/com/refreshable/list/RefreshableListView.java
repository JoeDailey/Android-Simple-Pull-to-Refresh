package com.refreshable.list;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;


public class RefreshableListView extends FrameLayout{
	
	public RefreshableListView(Context context, AttributeSet attrs, int defStyle) {super(context, attrs, defStyle); init(context);}
	public RefreshableListView(Context context, AttributeSet attrs) {super(context, attrs); init(context);}
	public RefreshableListView(Context context) {super(context); init(context);}
	
	private ProgressBar progress;
	private ListControl list;
	private onListRefreshListener ListrefreshLister;
	private onListLoadMoreListener ListLoadMoreListener;
	private int dragLength = 500;
	private int itemsFromBottom = 10;
	
	private void init(Context context){
		
		list = new ListControl(context);
		this.addView(list);
		
		ViewGroup.LayoutParams listParams = list.getLayoutParams();
		listParams.height = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
		listParams.width = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
		list.setLayoutParams(listParams);
		
		/////////////////////////////////////////////////
		LinearLayout top = new LinearLayout(context);
		top.setGravity(Gravity.TOP);
		top.setOrientation(LinearLayout.HORIZONTAL);
		this.addView(top);
		ViewGroup.LayoutParams topParams = top.getLayoutParams();
		topParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
		topParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
		top.setLayoutParams(topParams);
		
		////////////////////////////////////////////////
		FrameLayout left = new FrameLayout(context);
		progress = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
			progress.setProgress(100);
		FrameLayout right = new FrameLayout(context);
		
		top.addView(left);top.addView(progress);top.addView(right);

		/////////////////////////////////////////////////
		LinearLayout.LayoutParams leftParams = (LinearLayout.LayoutParams) left.getLayoutParams();
		leftParams.weight = 1;
		leftParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
		leftParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
		left.setLayoutParams(leftParams);
		
		LinearLayout.LayoutParams progressParams = (LinearLayout.LayoutParams) progress.getLayoutParams();
		progressParams.weight = 2;
		progressParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
		progressParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
		progress.setLayoutParams(progressParams);

		LinearLayout.LayoutParams rightParams = (LinearLayout.LayoutParams) right.getLayoutParams();
		rightParams.weight = 1;
		rightParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
		rightParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
		right.setLayoutParams(rightParams);
		/////////////////////////////////////////////////
		
		
		
		
		
	}
	
	public void setAdapter(ListAdapter adapter) {
		list.setAdapter(adapter);
	}
	public ListAdapter getAdapter() {
		return list.getAdapter();
	}
	public ListView getListView(){
		return list;
	}
	/**
	 * sets how many dpi the user must drag down to start the refresh
	 * @param dragLength standard is 500
	 */
	public void setDragLength(int dragLength){
		this.dragLength = dragLength;
	}
	/**
	 * sets the number of items from the bottom of the list to invoke more to load
	 * @param itemsFromBottom standard is 10
	 */
	public void setDistanceFromBottom(int itemsFromBottom) {
		this.itemsFromBottom = itemsFromBottom;
	}
	/**
	 * Invoked when refresh is requested
	 * @param ListrefreshLister
	 */
	public void setOnListRefreshListener(onListRefreshListener ListrefreshLister){
		this.ListrefreshLister = ListrefreshLister;
	}
	/**
	 * Invoked when load at bottom is requested
	 * @param listLoadMoreListener
	 */
	public void setOnListLoadMoreListener(onListLoadMoreListener listLoadMoreListener){
		this.ListLoadMoreListener = listLoadMoreListener;
	}
	/**
	 * Must be called to notify progress bar that operation is finished
	 */
	public void finishRefresh(){
		progress.setIndeterminate(false);
		progress.postInvalidate();
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) progress.getLayoutParams();
		params.weight = 2;
		progress.setLayoutParams(params);
		list.refreshing = false;
	}
	public void finishLoadingMore(){
		list.loadingMore = false;
	}
	
	class ListControl extends ListView implements OnScrollListener{

		public ListControl(Context context, AttributeSet attrs, int defStyle) {super(context, attrs, defStyle); init();}
		public ListControl(Context context, AttributeSet attrs) {super(context, attrs); init();}
		public ListControl(Context context) {super(context); init();}

		private void init(){
			
			this.setOnScrollListener(this);
		}
		boolean refreshing = false;
		@Override
		public boolean dispatchTouchEvent(MotionEvent event) {
			if(HittingTop){
				switch(event.getAction()){
					case MotionEvent.ACTION_DOWN:
						startY = event.getY();
						break;
					case MotionEvent.ACTION_MOVE:
						if(!refreshing)
							if(event.getY()-startY <= dragLength){
								double percent = 1 - (event.getY()-startY)/dragLength;
								double weight = 2*percent;
								LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) progress.getLayoutParams();
								params.weight = (float) weight;
								progress.setLayoutParams(params);
							}else{
								refreshing = true;
								ListrefreshLister.Refresh(RefreshableListView.this);
								startY = 100000.0;
								LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) progress.getLayoutParams();
								params.weight = 0;
								progress.setIndeterminate(true);
								progress.postInvalidate();
								progress.setLayoutParams(params);
							}
						break;
					case MotionEvent.ACTION_UP:
						startY = 100000.0;
						if(!refreshing){
							LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) progress.getLayoutParams();
							params.weight = 2;
							progress.setLayoutParams(params);
						}
				}
			}
			return super.dispatchTouchEvent(event);
		}

		private boolean HittingTop = false;
		private double startY = 100000.0;

		private boolean loadingMore = false;
		@Override
		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
			
				if(firstVisibleItem == 0)
					HittingTop = true;
				else{
					HittingTop = false;
					startY = 100000.0;
					if(!refreshing){
						LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) progress.getLayoutParams();
						params.weight = 2;
						progress.setLayoutParams(params);
					}
				}
				if(!loadingMore)
					if(this.getAdapter()!=null)
						if(!this.getAdapter().isEmpty())
							if(totalItemCount - (firstVisibleItem+visibleItemCount) <= itemsFromBottom)
								if(ListrefreshLister!=null)
									ListLoadMoreListener.LoadMore(RefreshableListView.this);
		}
		@Override public void onScrollStateChanged(AbsListView view, int scrollState) {}

	}
	public interface onListRefreshListener{
		public void Refresh(RefreshableListView list);
	}
	public interface onListLoadMoreListener{
		public void LoadMore(RefreshableListView list);
	}
}

