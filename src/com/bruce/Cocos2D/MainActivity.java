package com.bruce.Cocos2D;

import android.app.Activity;
import android.os.Bundle;
import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.opengl.CCGLSurfaceView;

public class MainActivity extends Activity {
    /**
     * Called when the activity is first created.
     */

    private CCDirector director;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CCGLSurfaceView surfaceView = new CCGLSurfaceView(this);
        setContentView(surfaceView);

        director = CCDirector.sharedDirector();//单例
        //核心方法1
        //开启绘制线程surfaceView完成绘制操作
        director.attachInView(surfaceView);

        //显示帧率,需加上系统的 fps_images.png
        director.setDisplayFPS(true);

        //屏幕方向的设置
        director.setDeviceOrientation(CCDirector.kCCDeviceOrientationLandscapeLeft);

        CCScene scene = createScene();
        //核心方法2
        director.runWithScene(scene);//director管理scene
    }

    private CCScene createScene() {
        CCScene root = CCScene.node();//不能New 源码n
//        FirstLayer firstLayer = new FirstLayer();
//          ActionLayer layer = new ActionLayer();
        DemoLayer layer = new DemoLayer();
        root.addChild(layer);
        return root;
    }

    @Override
    protected void onResume() {
        director.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        director.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        director.end();
        super.onDestroy();
    }
}
