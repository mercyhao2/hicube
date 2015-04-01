package cloudteam.hicube.view.xlistview;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.*;
import android.widget.AbsListView.OnScrollListener;
import cloudteam.hicube.R;
import cloudteam.hicube.utils.DensityUtil;

import java.text.SimpleDateFormat;


public class XListView extends ListView implements OnScrollListener {

  public static final String SP_NAME_XLISTVIEW = "sp_xlistview";// xlistview本地sp文件名
  public static final String SP_KEY_BASEREFRESHTIME = "refreshTime";// 刷新时间key

  private int scrollFooterTime = 0;// 滑动到底部的次数，如果滑动到底部再次滑动则执行加载更多
  private String spKeyRefreshTime = null;

  private float mLastY = -1; // save event y
  private Scroller mScroller; // used for scroll back
  private OnScrollListener mScrollListener; // user's scroll listener

  // the interface to trigger refresh and load more.
  private XListViewListener mListViewListener;

  // -- header view
  private XListViewHeader mHeaderView;
  // header view content, use it to calculate the Header's height. And hide it
  // when disable pull refresh.
  private RelativeLayout mHeaderViewContent;
  private TextView mHeaderTimeView;
  private int mHeaderViewHeight; // header view's height
  private int mHeaderViewMaxY;// header max height
  private boolean mEnablePullRefresh = true;
  private boolean mPullRefreshing = false; // is refreashing.

  // -- footer view
  private XListViewFooter mFooterView;
  private boolean mEnablePullLoad;
  private boolean mPullLoading;
  private boolean mIsFooterReady = false;

  // total list items, used to detect is at the bottom of listview.
  private int mTotalItemCount;

  // for mScroller, scroll back from header or footer.
  private int mScrollBack;
  private final static int SCROLLBACK_HEADER = 0;
  private final static int SCROLLBACK_FOOTER = 1;

  private final static int SCROLL_DURATION = 400; // scroll back duration
  private final static int PULL_LOAD_MORE_DELTA = 50; // when pull up >= 50px
  // at bottom, trigger
  // load more.
  private final static float OFFSET_RADIO = 1.8f; // support iOS like pull
  // feature.

  /**
   * @param context
   */
  public XListView(Context context) {
    super(context);
    initWithContext(context);
  }

  public XListView(Context context, AttributeSet attrs) {
    super(context, attrs);
    initWithContext(context);
  }

  public XListView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    initWithContext(context);
  }

  /**
   * 设置本地sp刷新时间key
   *
   * @param spKeyRefreshTime
   */
  private void setSpKeyRefreshTime(String spKeyRefreshTime) {
    this.spKeyRefreshTime = spKeyRefreshTime;
    mHeaderView.setSpKeyRefreshTime(spKeyRefreshTime);
  }

  private void initWithContext(Context context) {
    mScroller = new Scroller(context, new DecelerateInterpolator());
    // XListView need the scroll event, and it will dispatch the event to
    // user's listener (as a proxy).
    super.setOnScrollListener(this);

    // init header view
    mHeaderView = new XListViewHeader(context);
    mHeaderViewContent = (RelativeLayout) mHeaderView
            .findViewById(R.id.xlistview_header_content);
    mHeaderTimeView = (TextView) mHeaderView
            .findViewById(R.id.xlistview_header_time);
    addHeaderView(mHeaderView);

    // 1/2 height of screen
    mHeaderViewMaxY = DensityUtil.getHeight(context) / 2;

    // init footer view
    mFooterView = new XListViewFooter(context);

    // init header height
    mHeaderView.getViewTreeObserver().addOnGlobalLayoutListener(
            new OnGlobalLayoutListener() {
              @Override
              public void onGlobalLayout() {
                mHeaderViewHeight = mHeaderViewContent.getHeight();
                getViewTreeObserver()
                        .removeGlobalOnLayoutListener(this);
              }
            });

    setSpKeyRefreshTime(String.valueOf(getId()));

  }

  @Override
  public void setAdapter(ListAdapter adapter) {
    // make sure XListViewFooter is the last footer view, and only add once.
    if (mIsFooterReady == false) {
      mIsFooterReady = true;
      addFooterView(mFooterView);
    }
    super.setAdapter(adapter);
  }

  /**
   * enable or disable pull down refresh feature.
   *
   * @param enable
   */
  public void setPullRefreshEnable(boolean enable) {
    mEnablePullRefresh = enable;
    if (!mEnablePullRefresh) { // disable, hide the content
      mHeaderViewContent.setVisibility(View.GONE);
      // removeHeaderView(mHeaderView);
    } else {
      mHeaderViewContent.setVisibility(View.VISIBLE);
      // addHeaderView(mHeaderView);
    }
  }

  /**
   * enable or disable pull up load more feature.
   *
   * @param enable
   */
  public void setPullLoadEnable(boolean enable) {
    mEnablePullLoad = enable;

    if (getCount() > 2)
      // 有数据时显示Footer
      mFooterView.show();

    if (!mEnablePullLoad) {
      // mFooterView.hide();
      mFooterView.setState(XListViewFooter.STATE_ALL);
      mFooterView.setOnClickListener(null);
    } else {
      mPullLoading = false;
      mFooterView.setState(XListViewFooter.STATE_NORMAL);
      // both "pull up" and "click" will invoke load more.
      mFooterView.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
          startLoadMore();
        }
      });
    }
  }

  /**
   * stop refresh, reset header view.
   */
  public void stopRefresh() {
    if (mPullRefreshing == true) {
      mPullRefreshing = false;
      resetHeaderHeight();

      setRefreshTime();
    }
  }

  /**
   * stop load more, reset footer view.
   */
  public void stopLoadMore() {
    if (mEnablePullLoad) {
      if (mPullLoading == true) {
        mPullLoading = false;
        mFooterView.setState(XListViewFooter.STATE_NORMAL);
      }
    } else {
      mFooterView.setState(XListViewFooter.STATE_ALL);
    }
  }

  /**
   * set last refresh time
   *
   */
  public void setRefreshTime() {

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
    SharedPreferences s = getContext().getSharedPreferences(SP_NAME_XLISTVIEW, 0);
    Editor editor = s.edit();
    if (!TextUtils.isEmpty(spKeyRefreshTime)) {
      editor.putString(spKeyRefreshTime, simpleDateFormat.format(System.currentTimeMillis()));
      editor.commit();
    }
  }

  private void invokeOnScrolling() {
    if (mScrollListener instanceof OnXScrollListener) {
      OnXScrollListener l = (OnXScrollListener) mScrollListener;
      l.onXScrolling(this);
    }
  }

  private void updateHeaderHeight(float delta) {

    int newHeight = (int) delta + mHeaderView.getVisiableHeight();

    if ((newHeight + mHeaderView.getVisiableHeight()) > mHeaderViewMaxY) return;

    mHeaderView.setVisiableHeight(newHeight);
    if (mEnablePullRefresh && !mPullRefreshing) {
      if (mHeaderView.getVisiableHeight() > mHeaderViewHeight) {
        mHeaderView.setState(XListViewHeader.STATE_READY);
      } else {
        mHeaderView.setState(XListViewHeader.STATE_NORMAL);
      }
    }
    setSelection(0);
  }

  /**
   * reset header view's height.
   */
  private void resetHeaderHeight() {
    int height = mHeaderView.getVisiableHeight();
    if (height == 0) return;
    if (mPullRefreshing && height <= mHeaderViewHeight) return;
    int finalHeight = 0;
    if (mPullRefreshing && height > mHeaderViewHeight) finalHeight = mHeaderViewHeight;
    mScrollBack = SCROLLBACK_HEADER;
    mScroller.startScroll(0, height, 0, finalHeight - height, SCROLL_DURATION);
    invalidate();
  }

  private void updateFooterHeight(float delta) {
    int height = mFooterView.getBottomMargin() + (int) delta;
    if (mEnablePullLoad && !mPullLoading) {
      if (height > PULL_LOAD_MORE_DELTA) {
        mFooterView.setState(XListViewFooter.STATE_LOADING);
      } else {
        mFooterView.setState(XListViewFooter.STATE_NORMAL);
      }
    }
    mFooterView.setBottomMargin(height);

    // setSelection(mTotalItemCount - 1); // scroll to bottom
  }

  private void resetFooterHeight() {
    int bottomMargin = mFooterView.getBottomMargin();
    if (bottomMargin > 0) {
      mScrollBack = SCROLLBACK_FOOTER;
      mScroller.startScroll(0, bottomMargin, 0, -bottomMargin,
              SCROLL_DURATION);
      invalidate();
    }
  }

  private void startLoadMore() {
    mPullLoading = true;
    mFooterView.setState(XListViewFooter.STATE_LOADING);
    if (mListViewListener != null) {
      mListViewListener.onListViewLoadMore(this);
    }
  }

  public void startRefresh() {
    if (getFirstVisiblePosition() != 0) setSelection(0);// 滑到最上方

    mPullRefreshing = true;
    mHeaderView.setState(XListViewHeader.STATE_REFRESHING);
    mHeaderView.setVisiableHeight(mHeaderViewContent.getLayoutParams().height);

    if (mListViewListener != null) mListViewListener.onListViewRefresh(this);

  }

  @Override
  public boolean onTouchEvent(MotionEvent ev) {

    if (mLastY == -1) mLastY = ev.getRawY();

    switch (ev.getAction()) {
      case MotionEvent.ACTION_DOWN:
        mLastY = ev.getRawY();
        break;
      case MotionEvent.ACTION_MOVE:
        final float deltaY = ev.getRawY() - mLastY;
        mLastY = ev.getRawY();
        if (getFirstVisiblePosition() == 0 && (mHeaderView.getVisiableHeight() > 0 || deltaY > 0)) {
          // the first item is showing, header has shown or pull down.
          updateHeaderHeight(deltaY / OFFSET_RADIO);
          invokeOnScrolling();
        } else if (getLastVisiblePosition() == mTotalItemCount - 1
                && (mFooterView.getBottomMargin() > 0 || deltaY < 0)) {
          // last item, already pulled up or want to pull up.
          // updateFooterHeight(-deltaY / OFFSET_RADIO);
        }
        break;
      default:
        mLastY = -1; // reset
        if (getFirstVisiblePosition() == 0) {
          if (mEnablePullRefresh && mHeaderView.getVisiableHeight() > mHeaderViewHeight) {
            mPullRefreshing = true;
            mHeaderView.setState(XListViewHeader.STATE_REFRESHING);
            if (mListViewListener != null) {
              mListViewListener.onListViewRefresh(this);
            }
          }
          resetHeaderHeight();
        }
        if (getLastVisiblePosition() == mTotalItemCount - 1) {
          // invoke load more.
          // 上拉到最底部再次上拉时自动加载更多
          // if (mEnablePullLoad && scrollFooterTime == 1) {
          // startLoadMore();
          // scrollFooterTime = 0;
          // } else {
          // scrollFooterTime++;
          // }
          // resetFooterHeight();
        }
        break;
    }
    return super.onTouchEvent(ev);
  }

  @Override
  public void computeScroll() {
    if (mScroller.computeScrollOffset()) {
      if (mScrollBack == SCROLLBACK_HEADER) {
        mHeaderView.setVisiableHeight(mScroller.getCurrY());
      } else {
        mFooterView.setBottomMargin(mScroller.getCurrY());
      }
      postInvalidate();
      invokeOnScrolling();
    }
    super.computeScroll();
  }

  @Override
  public void setOnScrollListener(OnScrollListener l) {
    mScrollListener = l;
  }

  @Override
  public void onScrollStateChanged(AbsListView view, int scrollState) {
    if (mScrollListener != null) {
      mScrollListener.onScrollStateChanged(view, scrollState);
    }
  }

  @Override
  public void onScroll(AbsListView view, int firstVisibleItem,
                       int visibleItemCount, int totalItemCount) {
    mTotalItemCount = totalItemCount;
    if (mScrollListener != null) {
      mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount,
              totalItemCount);
    }
  }

  public void setXListViewListener(XListViewListener l) {
    mListViewListener = l;
  }

  /**
   * you can listen ListView.OnScrollListener or this one. it will invoke
   * onXScrolling when header/footer scroll back.
   */
  public interface OnXScrollListener extends OnScrollListener {
    public void onXScrolling(View view);
  }

  /**
   * implements this interface to get refresh/load more event.
   */
  public interface XListViewListener {
    public void onListViewRefresh(XListView listView);

    public void onListViewLoadMore(XListView listView);
  }
}
