package com.example.pch.tabapplication;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }


    /**
     *
     * @param
     * @return
     */

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_main, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();

            //noinspection SimplifiableIfStatement
            if (id == R.id.action_settings) {
                return true;
            }

            return super.onOptionsItemSelected(item);
        }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        static final String[] LIST_MENU = {"으악", "졸라게", "어렵네", "엿같은", "안드로이드", "스튜디오", "ㅗㅗ"} ;

        public class GalleryAdapter extends BaseAdapter {
            int CustomGalleryItemBg;
            String mBasePath;
            Context mContext;
            String[] mImgs;
            Bitmap bm;
            DataSetObservable mDataSetObservable = new DataSetObservable(); // DataSetObservable(DataSetObserver)의 생성

            public String TAG = "Gallery Adapter Example :: ";

            public GalleryAdapter(Context context, String basepath) {
                this.mContext = context;
                this.mBasePath = basepath;

                File file = new File(mBasePath);
                if (!file.exists()) {
                    if (!file.mkdirs()) {
                        Log.d(TAG, "failed to create directory");
                    }
                }
                mImgs = file.list();

                TypedArray array = mContext.obtainStyledAttributes(R.styleable.GalleryTheme);
                CustomGalleryItemBg = array.getResourceId(R.styleable.GalleryTheme_android_galleryItemBackground, 0);
                array.recycle();
            }

            @Override
            public int getCount() {
                File dir = new File(mBasePath);
                mImgs = dir.list();
                return mImgs.length;
            }

            @Override
            public Object getItem(int position) {
                return position;
            }

            // Adapter 내 Item에서 직접 주소를 받아오도록 method 추가.
            // 이전에는 MainActivity와 주소 및 position이 달라 비정상적인 앱의 종료가 발생한 것으로 보인다
            public String getItemPath(int position) {
                String path = mBasePath + File.separator + mImgs[position];
                return path;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            // Override this method according to your need
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                ImageView imageView;
                if (convertView == null) {
                    // if it's not recycled, initialize some attributes
                    imageView = new ImageView(mContext);
                } else {
                    imageView = (ImageView) convertView;
                }
                bm = BitmapFactory.decodeFile(mBasePath + File.separator + mImgs[position]);
                Bitmap mThumbnail = ThumbnailUtils.extractThumbnail(bm, 300, 300);
                imageView.setPadding(8, 8, 8, 8);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setLayoutParams(new GridView.LayoutParams(GridView.LayoutParams.MATCH_PARENT, GridView.LayoutParams.MATCH_PARENT));
                imageView.setImageBitmap(mThumbnail);
                return imageView;
            }

            @Override
            public void registerDataSetObserver(DataSetObserver observer) { // DataSetObserver의 등록(연결)
                mDataSetObservable.registerObserver(observer);
            }

            @Override
            public void unregisterDataSetObserver(DataSetObserver observer) { // DataSetObserver의 해제
                mDataSetObservable.unregisterObserver(observer);
            }

            @Override
            public void notifyDataSetChanged() { // 위에서 연결된 DataSetObserver를 통한 변경 확인
                mDataSetObservable.notifyChanged();
            }

        }

        public String basePath = null;
        public GridView mGridView;
        public GalleryAdapter mGalleryAdapter;




        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);

            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // 텍스트 뷰
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);



            switch(getArguments().getInt(ARG_SECTION_NUMBER))
            {
                case 1://갤러리 뷰
                    File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES), "Screenshots");

                    if (! mediaStorageDir.exists()){
                        if (! mediaStorageDir.mkdirs()){
                            Log.d("MyCameraApp", "failed to create directory");
                        }
                    }

                    basePath = mediaStorageDir.getPath();
                    //basePath = Environment.getExternalStorageDirectory()+"/Pictures/Screenshots/";
                    View view2 = inflater.inflate(R.layout.gallery_main, null) ;
                    mGalleryAdapter = new GalleryAdapter(getActivity(), basePath); // 앞에서 정의한 Custom Image Adapter와 연결
                    mGridView = (GridView)view2.findViewById(R.id.gridview); // .xml의 GridView와 연결
                    mGridView.setAdapter(mGalleryAdapter); // GridView가 Custom Image Adapter에서 받은 값을 뿌릴 수 있도록 연결

                    return mGridView;

                case 2://리스트 뷰
                    View view = inflater.inflate(R.layout.list_main, null) ;
                    ArrayAdapter Adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, LIST_MENU) ;

                    ListView listview = (ListView) view.findViewById(R.id.listview1) ;
                    listview.setAdapter(Adapter) ;
                    listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView parent, View v, int position, long id) {

                            // get TextView's Text.
                            String strText = (String) parent.getItemAtPosition(position) ;

                            // TODO : use strText
                        }
                    }) ;
                    return listview;
                case 3:
                    //A.concat("HAVEN WORLD");
                    textView.setText("HAVEN WORLD");
                    return rootView;
            }


            return null;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Gallery";
                case 1:
                    return "List";
                case 2:
                    return "HAVEN";
            }
            return null;
        }
    }
}
