package com.bruce.Cocos2D;

import org.cocos2d.actions.base.CCRepeatForever;
import org.cocos2d.actions.interval.*;
import org.cocos2d.layers.CCLayer;
import org.cocos2d.nodes.CCLabel;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.ccColor3B;

/**
 * Created by Bruce
 * Data 2014/8/21
 * Time 18:48.
 */
public class ActionLayer extends CCLayer {
    public ActionLayer() {
//        move();
//    scale(); //缩放动画
        //rotate  旋转动画
        // tint();//渐变动画

        jump1();//组合动画
    }

    private void jump1() {
        CGPoint pos = CGPoint.ccp(200,300);
        CCJumpBy jumpBy = CCJumpBy.action(2,pos,150,2);

        CCRotateBy rotateBy = CCRotateBy.action(1,360);

        //动作并行！！
        CCSpawn spawn = CCSpawn.actions(jumpBy,rotateBy);
        //动作串联！！
        CCSequence sequence = CCSequence.actions(spawn,spawn.reverse(),CCDelayTime.action(1));

        CCRepeatForever forever = CCRepeatForever.action(sequence);

        CCSprite sprite = getSprite();
        sprite.setAnchorPoint(0.5f,0.5f);
        sprite.setPosition(100,0);
        sprite.runAction(forever);
    }

    private void tint() {

        ccColor3B c = ccColor3B.ccc3(0, -100, 0);
        CCTintBy tintBy = CCTintBy.action(1, c);

        CCLabel label = CCLabel.makeLabel("我被锁定发生发生发生发生发生阿斯顿发按时", "", 25);
        label.setAnchorPoint(0, 0);
        label.setPosition(200, 100);
        label.setColor(ccColor3B.ccc3(200, 250, 0));
        this.addChild(label);
        label.runAction(CCRepeatForever.action(CCSequence.actions(tintBy, tintBy.reverse())));
    }

    private void scale() {

        CCScaleBy scaleBy = CCScaleBy.action(0.2f, 2f);
        CCSprite sprite = getSprite();
        sprite.setPosition(200, 200);
        CCSequence sequence = CCSequence.actions(scaleBy, scaleBy.reverse());
        CCRepeatForever forever = CCRepeatForever.action(sequence);
        sprite.runAction(forever);
    }

    private void move() {
        CGPoint point = CGPoint.ccp(300, 150);
        //action(t,p) t表示秒
        CCMoveTo moveTo = CCMoveTo.action(2, point);
        getSprite().runAction(moveTo);

        CGPoint point1 = CGPoint.ccp(300, 150);
        //action(t,p) t表示秒
        CCMoveBy by = CCMoveBy.action(2, point1);

        CCSequence sequence = CCSequence.actions(by, by.reverse());
        CCSprite sprite = getSprite();
        sprite.setPosition(100, 0);
        sprite.runAction(sequence);
    }


    private CCSprite getSprite() {
        CCSprite sprite = CCSprite.sprite("zombhead.png");
        sprite.setAnchorPoint(0, 0);
        this.addChild(sprite);
        return sprite;
    }
}
