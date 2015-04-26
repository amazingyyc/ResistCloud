package com.resistcloud.constant;

/*
 * “Ù¿÷≤•∑≈¿‡
 */
import com.resistcloud.main.MainActivity;

import com.resistcloud.R;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

public class Pool 
{
	MainActivity activity;
	
	public SoundPool soundPool;
	public boolean   ifSound;
	
	int touchUpID;
	int bulletID;
	int circleID;
	int glbodyID;
	int loseID;
	int winID;
	
	float volumn;
	
	public Pool(MainActivity activity)
	{
		this.activity = activity;
		this.ifSound  = true;
		
		initSound();
		
		AudioManager mgr = (AudioManager)activity.getSystemService(Context.AUDIO_SERVICE);
		float volumnCurr = mgr.getStreamVolume   (AudioManager.STREAM_MUSIC);
    	float volumnMax  = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
    	volumn=volumnCurr/volumnMax;
	}
	
	private void initSound()
	{
		this.soundPool = new SoundPool(6, AudioManager.STREAM_MUSIC, 0);
		
		this.touchUpID = this.soundPool.load(this.activity, R.raw.touchup, 1);
		this.bulletID  = this.soundPool.load(this.activity, R.raw.bullet , 1);
		this.circleID  = this.soundPool.load(this.activity, R.raw.circle , 1);
		this.glbodyID  = this.soundPool.load(this.activity, R.raw.glbody , 1);
		this.loseID    = this.soundPool.load(this.activity, R.raw.lose   , 1);
		this.winID     = this.soundPool.load(this.activity, R.raw.win    , 1);
	}
	
	public void playTouchUp()
	{
		if(!ifSound)
			return;
		
		soundPool.play(touchUpID, volumn, volumn, 1, 0, 1.0f);
	}
	
	public void playBullet()
	{
		if(!ifSound)
			return;
    	
		soundPool.play(bulletID, volumn, volumn, 1, 0, 1.0f);
	}
	
	public void playCircle()
	{
		if(!ifSound)
			return;
    	
		soundPool.play(circleID, volumn, volumn, 1, 0, 1.0f);
	}

	public void playGLBody()
	{
		if(!ifSound)
			return;

		soundPool.play(glbodyID, volumn, volumn, 1, 0, 1.0f);
	}
	
	public void playLose()
	{
		if(!ifSound)
			return;
    	
		soundPool.play(loseID, volumn, volumn, 1, 0, 1.0f);
	}
	
	public void playWin()
	{
		if(!ifSound)
			return;
    	
		soundPool.play(winID, volumn, volumn, 1, 0, 1.0f);
	}
}











