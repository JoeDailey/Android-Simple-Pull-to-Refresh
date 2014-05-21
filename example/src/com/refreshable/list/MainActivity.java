package com.refreshable.list;
/**
 * Written by Joe Dailey
 * Style Based on Chris Banes
 * General Idea by Loren Brichter
 *
 * For Android api 1+
 *
 * No License restriction held by me
 * Look to influences for further licensing
 */
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.refreshable.list.R;
import com.refreshable.list.RefreshableListView.onListLoadMoreListener;
import com.refreshable.list.RefreshableListView.onListRefreshListener;

public class MainActivity extends Activity implements onListRefreshListener, onListLoadMoreListener{

	public String[] GarbageText = {"Lorem ipsum dolor sit amet", "consectetur adipiscing elit",
			"Duis dictum leo", "non imperdiet rutrum",
			"Nullam hendrerit turpis ac tristique interdum",
			"Vestibulum pellentesque", "lacus quis adipiscing tincidunt",
			"Sed lobortis nibh", "eu erat pellentesque", "eu auctor lectus faucibus",
			"Suspendisse ullamcorper", "nisl et tellus", "porttitor commodo",
			"Quisque non dolor", "vel justo", "hendrerit fringilla",
			"Donec ut urna vel", "sapien accumsan", "commodo et et felis",
			"Sed vitae velit", "in magna semper hendrerit",
			"Suspendisse nec libero", "adipiscing vulputate", "ipsum sed", "pulvinar enim"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//RefreshableList lines start
		RefreshableListView List = (RefreshableListView) findViewById(R.id.RefreshList);
		List.setOnListRefreshListener(this);//---------------------------------------------------------------Important
		List.setOnListLoadMoreListener(this);
		//RefreshableList Lines end

		//populate List start
		ListAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, GarbageText);
		List.setAdapter(adapter);
		//populate List End

		//or...(also allows for access to all ListView methods)
		//List.getListView().setAdapter(adapter);
		//List.getListView().setBackground(background);
		
		//or set specific attributes
		//List.setDragLength(500);
		//List.setDistanceFromBottom(10);
	}

	@Override
	public void Refresh(RefreshableListView list) {
		Toast.makeText(this, "Refreshing...", Toast.LENGTH_SHORT).show();
		fakeDownload(list);
		//^^^download whatever
	}

	@Override
	public void LoadMore(RefreshableListView list) {
		Toast.makeText(this, "Loading More...", Toast.LENGTH_SHORT).show();
		fakeDownload(list);
		//^^^download whatever
	}
	private void fakeDownload(RefreshableListView list){

		////This just asyncly waits 3 seconds then does the finishRefresh()
		new AsyncTask<RefreshableListView, Object, RefreshableListView>(){
			protected RefreshableListView doInBackground(RefreshableListView... params) {
				try {Thread.sleep(3000);} catch (InterruptedException e) {}
				return params[0];

			}
			@Override
			protected void onPostExecute(RefreshableListView list) {
				//I just finish both here to not have to write two example mocks
				list.finishRefresh();//-------------------------------------------------------------------------Important
				list.finishLoadingMore();//---------------------------------------------------------------------Important
				super.onPostExecute(list);
			}
		}.execute(list);
	}
}
