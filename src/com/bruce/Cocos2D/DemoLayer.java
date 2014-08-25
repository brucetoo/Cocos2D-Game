package com.bruce.Cocos2D;

import android.view.MotionEvent;
import org.cocos2d.actions.base.CCRepeatForever;
import org.cocos2d.actions.instant.CCCallFunc;
import org.cocos2d.actions.instant.CCPlace;
import org.cocos2d.actions.interval.CCAnimate;
import org.cocos2d.actions.interval.CCMoveTo;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.layers.CCLayer;
import org.cocos2d.layers.CCTMXObjectGroup;
import org.cocos2d.layers.CCTMXTiledMap;
import org.cocos2d.nodes.*;
import org.cocos2d.particlesystem.CCParticleSnow;
import org.cocos2d.particlesystem.CCParticleSystem;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGRect;
import org.cocos2d.types.CGSize;
import org.cocos2d.types.util.CGPointUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Bruce
 * Data 2014/8/22
 * Time 13:33.
 */
public class DemoLayer extends CCLayer {

    private CCTMXTiledMap map;
    private List<CGPoint> roads;

    public DemoLayer() {

        setIsTouchEnabled(true);//设置屏幕点击
        //加载地图
        loadMap();
        //获取图片路径
        loadRoad();
        //移动物体
        move();
        //物体自身的序列帧
        animate();
        //定义粒子系统
        particleSystem();
        //处理声音引擎
        playMusic();
        //地图的移动
        /*
        1.大地图的移动： this.runAction(CCFollow.action(sprite)); 跟随者sprite移动，使其始终在中间
        2.小地图移动:加载地图时，地图的锚点设置到中心点，修改地图的坐标中点。在重新ccTouchesMoved 添加map.touchMove(event,map);
         地图在移动是 人物也要绑定到地图上,而不是layer  getSprite 中 map.addChild(sprite);
         */
        //游戏暂停的处理
    }


    @Override
    public boolean ccTouchesBegan(MotionEvent event) {
        //冻结主layer
        this.onExit();  //冻结+setIsTouchEnabled(false)
        //显示暂停layer
        PauseLayer pauseLayer = new PauseLayer();
        //将暂停layer添加到场景中，
        this.getParent().addChild(pauseLayer);
        return super.ccTouchesBegan(event);
    }

    /**
     * 暂停的layer
     */
    private class PauseLayer extends CCLayer{
        CCSprite heart;
        private PauseLayer() {
            //让新添加的layer可点击
            this.setIsTouchEnabled(true);
            heart = getSprite();
            CGSize winSize = CCDirector.sharedDirector().getWinSize();
            heart.setPosition(winSize.getWidth()/2,winSize.getHeight()/2);
        }

        public CCSprite getSprite(){
            CCSprite heart = CCSprite.sprite("heart.png");
            this.addChild(heart);
            return heart;
        }

        /**
         * 点击图片重新开始游戏
         * @param event
         * @return
         */
        @Override
        public boolean ccTouchesBegan(MotionEvent event) {
            //获取点击的点
            CGPoint point = convertTouchToNodeSpace(event);
            //判断点击的点是否在 sprite的矩形区域下
            if(CGRect.containsPoint(heart.getBoundingBox(),point)){
                //销毁当前layer
                this.removeSelf();
                //继续游戏
                DemoLayer.this.onEnter();
            }
            return super.ccTouchesBegan(event);
        }
    }

    /**
     * 触摸屏蔽移动地图
     * @param event
     * @return
     */
    @Override
    public boolean ccTouchesMoved(MotionEvent event) {
        map.touchMove(event,map);
        return super.ccTouchesMoved(event);
    }

    private void playMusic() {
    //CCDirector.theApp  已经将 context封装起来了
      //  SoundEngine.sharedEngine().playSound(CCDirector.theApp,R.raw.wind,false);
    }


    /**
     * 粒子系统的处理
     */
    private void particleSystem() {
       //获取雪花的粒子系统
        CCParticleSystem system = CCParticleSnow.node();
        //添加雪花的图片
        system.setTexture(CCTextureCache.sharedTextureCache().addImage("snow1.png"));
        //添加粒子系统到layer
        this.addChild(system);
        //粒子系统 的控制
//        system.stopSystem();
    }

    /**
     * 序列帧的播放
     */
    private void animate() {

        ArrayList<CCSpriteFrame> frames = new ArrayList<CCSpriteFrame>();
       // String filename = "z_1_0%d.png";
        /**
         * 因为需要很多帧图片，所以需要对其名字进行格式化处理
         * z_1_%02d.png 可处理 1-99张图片
         * 2表示占两位，0 表示不足两位补0
         */
        String filename = "z_1_%02d.png";
        for(int i=1;i<=7;i++){
            CCSpriteFrame spriteFrame = CCSprite.sprite(String.format(filename,i)).displayedFrame();
            frames.add(spriteFrame);
        }
        CCAnimation anim = CCAnimation.animation("",0.5f,frames);
        CCAnimate animate = CCAnimate.action(anim);
        CCRepeatForever forever = CCRepeatForever.action(animate);
        sprite.runAction(forever);
    }

    /**
     * 加工地图
     */
    private void loadMap() {
        map = CCTMXTiledMap.tiledMap("map.tmx");

        /**
         * 需要手动移动地图，需要将地图的锚点设置到中心点，修改地图的坐标中点
         */
        map.setAnchorPoint(0.5f,0.5f);
        CGSize size = map.getContentSize();
        map.setPosition(size.getWidth()/2,size.getHeight()/2);
        this.addChild(map);
    }


    private CCSprite sprite;


    public void move() {
        sprite = getSprite();
        //移动到初始位置
        sprite.setPosition(roads.get(0));
        playMusic();
        moveToNext();
        //  sprite.runAction(to);
        //利用递归操作循环执行全部点
     //   CCSequence sequence = CCSequence.actions(to, CCCallFunc.action(this,"moveToNext"));
    }

    private int current = 0;
    private int speed = 100;

    /**
     * 移动到下一个位置
     */
    public void moveToNext(){
       current++;
        if(current < roads.size()){
            //计算两个点之间的距离，保持匀速
            float time = CGPointUtil.distance(sprite.getPosition(),roads.get(current))/speed;
            CCMoveTo moveTo = CCMoveTo.action(time,roads.get(current));
            //CCRotateBy rotateTo = CCRotateBy.action(time, 360);
            //CCSpawn spawn = CCSpawn.actions(moveTo,rotateTo);
            CCSequence sequence = CCSequence.actions(moveTo, CCCallFunc.action(this,"moveToNext"));
            sprite.runAction(sequence);
        }else{
            current = 0;
            CCPlace place = CCPlace.action(roads.get(current));
            CCSequence sequence = CCSequence.actions(place, CCCallFunc.action(this,"moveToNext"));
            sprite.runAction(sequence);
        }
    }

    /**
     * 解析加工的地图，获取路径上的点
     */
    private void loadRoad() {

        roads = new ArrayList<CGPoint>();
        //获取对象组中名字为road的对象组
        CCTMXObjectGroup objectGroup = map.objectGroupNamed("road");
        //拿到对象组中的object
        ArrayList<HashMap<String, String>> objects = objectGroup.objects;
        //获取每个点
        for (HashMap<String, String> item : objects) {
            int x = Integer.valueOf(item.get("x"));
            int y = Integer.valueOf(item.get("y"));
            roads.add(CGPoint.ccp(x, y));
        }
    }

    private CCSprite getSprite() {
        CCSprite sprite = CCSprite.sprite("z_1_01.png");
        sprite.setFlipX(true);
        //sprite.setScale(0.5f);
        /**
         * 地图 和 人物绑定到一起
         */
        map.addChild(sprite);
        return sprite;
    }
}
