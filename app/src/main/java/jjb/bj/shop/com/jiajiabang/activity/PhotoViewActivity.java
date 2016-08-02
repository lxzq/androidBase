package jjb.bj.shop.com.jiajiabang.activity;



import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import android.view.View;

import android.view.ViewGroup;
import android.widget.Button;

import android.widget.TextView;



import com.nostra13.universalimageloader.core.ImageLoader;

import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;


import jjb.bj.shop.com.jiajiabang.R;
import jjb.bj.shop.com.jiajiabang.tools.OSSImageTools;
import uk.co.senab.photoview.PhotoView;

public class PhotoViewActivity extends BaseActivity {

    private ViewPager viewPager;
    private TextView textView;
    private Context mContext;
    private Button button;
    public static final String PHOTO_IMAGE_LIST = "image_list";

    private ArrayList<String> imagesList;
    private int index = 1;
    private ArrayList<PhotoView> viewList;
    private MyPageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_view);
        imagesList = getIntent().getStringArrayListExtra(PHOTO_IMAGE_LIST);
        mContext = this;
        viewPager = (ViewPager)findViewById(R.id.view_page) ;
        textView = (TextView)findViewById(R.id.image_num) ;
        button = (Button)findViewById(R.id.back);
        viewList = new ArrayList<>();
        loadImage();
    }

    private void loadImage(){
        adapter = new MyPageAdapter();
        for (int i = 0 ; i < imagesList.size() ; i++){
            String path = OSSImageTools.getImage(imagesList.get(i));
            ImageLoader.getInstance().loadImage(path, new SimpleImageLoadingListener(){
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    PhotoView photoView = new PhotoView(mContext);
                    photoView.setImageBitmap(loadedImage);
                    viewList.add(photoView);
                    adapter.notifyDataSetChanged();
            }
            });
        }
        textView.setText(index +"/"+imagesList.size());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
         viewPager.setAdapter(adapter);
         viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                index = position + 1 ;
                textView.setText(index +"/"+imagesList.size());
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private class MyPageAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return viewList.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
             container.addView(viewList.get(position));
             return viewList.get(position);
        }
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }



}
