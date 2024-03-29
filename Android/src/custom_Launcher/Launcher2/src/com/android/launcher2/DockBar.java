package com.android.launcher2;

import java.net.URISyntaxException;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.launcher.R;
import com.android.launcher2.DragController.DragListener;


public class DockBar extends LinearLayout implements DropTarget, DragSource,
DragController.DragListener,View.OnLongClickListener {

   
    private Launcher mLauncher;
    private DragController mDragController;
    private Workspace mWorkspace;
    private View mDragView;
    private LinearLayout mItemHolder;
    private View mScrollView;
    private View mSelectedView;
    
    public DockBar(Context context){       
	super(context);   
	// TODO Auto-generated constructor stub
	
	}    
  
    public DockBar(Context context, AttributeSet attrs) {      
    super(context, attrs);                
	}     
    @Override
    protected void onFinishInflate() {
        // TODO Auto-generated method stub
	 Log.i("Launcher", "DockBar->onFinishInflate");
	mItemHolder  = (LinearLayout)findViewById(R.id.dock_item_holder);
	mScrollView= findViewById(R.id.dock_scroll_view); 
        super.onFinishInflate();
    }
   
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //
        FrameLayout.LayoutParams lp= (FrameLayout.LayoutParams)mItemHolder.getLayoutParams();
        lp.gravity=Gravity.CENTER;
        mItemHolder.setLayoutParams(lp);
        postInvalidate();
        requestLayout();
    }
    
    @Override
    public boolean acceptDrop(DragSource source, int x, int y, int xOffset,int yOffset,DragView dragView, Object dragInfo) {                 
	//      
	 Log.i("Launcher", "DockBar->acceptDrop");
	final ItemInfo item = (ItemInfo) dragInfo;       
	if (item.itemType == LauncherSettings.Favorites.ITEM_TYPE_APPWIDGET                
	|| item.itemType == LauncherSettings.Favorites.ITEM_TYPE_LIVE_FOLDER                
	|| item.itemType == LauncherSettings.Favorites.ITEM_TYPE_USER_FOLDER                 
	|| item.itemType == LauncherSettings.Favorites.ITEM_TYPE_WIDGET_PHOTO_FRAME                
	|| item.itemType == LauncherSettings.Favorites.ITEM_TYPE_WIDGET_SEARCH                
	|| item.itemType == LauncherSettings.Favorites.ITEM_TYPE_WIDGET_CLOCK) {         
	return false;        
	}  
	return true; 
	} 
    @Override
    public Rect estimateDropLocation(DragSource source, int x, int y,
	    int xOffset, int yOffset, DragView dragView, Object dragInfo,
	    Rect recycle) {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public void onDragEnter(DragSource source, int x, int y, int xOffset,
	    int yOffset, DragView dragView, Object dragInfo) {
	// TODO Auto-generated method stub
	 Log.i("Launcher", "DockBar->onDragEnter");
	setBackgroundResource(R.drawable.dock_bg_press); 
    }

    @Override
    public void onDragExit(DragSource source, int x, int y, int xOffset,
	    int yOffset, DragView dragView, Object dragInfo) {
	// TODO Auto-generated method stub
	setBackgroundResource(R.drawable.dock_bg); 
	mItemHolder.removeView(mDragView);
	 Log.i("Launcher", "DockBar->onDragExit");
    }

    @Override
    public void onDragOver(DragSource source, int x, int y, int xOffset,
	    int yOffset, DragView dragView, Object dragInfo) {
	// TODO Auto-generated method stub
	
    }

    private void addItemAt(ItemInfo itemInfo, int position)
    {
	View view=null;
	switch (itemInfo.itemType) {
	case LauncherSettings.Favorites.ITEM_TYPE_APPLICATION:
	case LauncherSettings.Favorites.ITEM_TYPE_SHORTCUT:	
	    ShortcutInfo shortcutInfo;
	// 
	if(itemInfo.container ==NO_ID&& itemInfo instanceof ApplicationInfo)	    
	{
	    //
	    shortcutInfo= new ShortcutInfo((ApplicationInfo)itemInfo);
	}
	else
	shortcutInfo = (ShortcutInfo)itemInfo; //
	//
	view = mLauncher.CreateDockShortcut(shortcutInfo);
	 view.setOnLongClickListener(this);	
	  break;
	case LauncherSettings.Favorites.ITEM_TYPE_USER_FOLDER:
	    break;
	default:
	    throw new IllegalStateException("Unknown item type: "
		    + itemInfo.itemType);
	}	 
	mItemHolder.addView(view, position);	  
    }
    
    /*
     * 
     */
    //add by Jay 
    public int count = 0;

    public int getLocation(int x){   
	    Log.i("launcher","getChildCount:"+mItemHolder.getChildCount());
	       for(int i=0;i<mItemHolder.getChildCount();i++){   
	           View iv = mItemHolder.getChildAt(i); 
				    Log.i("launcher","iv width:"+iv.getWidth());

	           int[] position = new int[2];
	           //
	           iv.getLocationOnScreen(position);
	           //
			Log.i("launcher","position: "+ position[0] + position[1]);
	            if(x<=(position[0]+(iv.getWidth()/2))){   	                
	                    return i;   	               
	            }   	               
	        }    
	        return mItemHolder.getChildCount();   
//	       return ++count;
	    }  
    
    
    /*
     * 
     */
    @Override
    public void onDrop(DragSource source, int x, int y, int xOffset,
	    int yOffset, DragView dragView, Object dragInfo) {
	int position=0;
	Log.i("launcher","x: "+x+",y: "+y);	
	position=getLocation(x); //
	Log.i("launcher","location: "+position);	
	addItemAt((ItemInfo)dragInfo, position); 	
    }

    @Override
    public void onDropCompleted(View target, boolean success) {
	// TODO Auto-generated method stub
    }

    @Override
    public void setDragController(DragController dragController) {
	// TODO Auto-generated method stub
	mDragController = dragController;
    }

    @Override
    public void onDragEnd() {
	// TODO Auto-generated method stub

    }

    @Override
    public void onDragStart(DragSource source, Object dragInfo, int dragAction) {
	// TODO Auto-generated method stub
	Log.i("Launcher", "DockBar->onDragStart");
    }

    
    void setLauncher(Launcher launcher) {
	mLauncher = launcher;
    }

    void setWorkspace(Workspace workspace )
    {
	mWorkspace=workspace;
    }

   
    @Override
    public boolean onLongClick(View v) {
	// TODO Auto-generated method stub
	 if (mLauncher.isAllAppsVisible())
	    mLauncher.closeAllApps(false);
	    mSelectedView = v;	   
	mDragController.startDrag(v, this, v.getTag(),
		DragController.DRAG_ACTION_MOVE);
	 removeSelectedItem();
	    return true;
    }

    private void removeSelectedItem()
    {
      if (mSelectedView == null)
        return;
      mItemHolder.removeView(mSelectedView);
    }

   
}
