package com.bruce.Cocos2D;

import org.cocos2d.layers.CCLayer;
import org.cocos2d.nodes.CCSprite;

/**
 * Created by Bruce
 * Data 2014/8/21
 * Time 15:21.
 */
public class FirstLayer extends CCLayer {

    public FirstLayer() {
        //图片资源放在asserts下
        CCSprite sprite = CCSprite.sprite("zombhead.png");
        this.addChild(sprite);
        //设置锚点~默认在图片中间
        sprite.setAnchorPoint(0,0);

        flipX();
    }

    private void flipX() {
        CCSprite sprite = CCSprite.sprite("zombhead.png");
        sprite.setFlipX(true);//设置x轴的镜像

        sprite.setAnchorPoint(0,0);
        sprite.setPosition(100,0);
        this.addChild(sprite);
    }
}
