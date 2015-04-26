package com.resistcloud.main;

/*
 * 主界面
 */
import com.resistcloud.constant.Eye;
import com.resistcloud.constant.GLConstant;
import com.resistcloud.constant.Pool;
import com.resistcloud.view.HelpSurfaceView;
import com.resistcloud.view.MenuSurfaceView;
import com.resistcloud.view.SelectSurfaceView;
import com.resistcloud.view.SetSurfaceView;
import com.resistcloud.view.TagSurfaceView;
import com.resistcloud.worldbody.GLWorld;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity
{
	public static final int GAME_MENU = 0;
	public static final int GAME_SELECT = 1;
	public static final int GAME_SET = 2;
	public static final int GAME_HELP = 3;
	public static final int GAME_START = 4;
	public static final int GAME_ONWIN = 5;
	public static final int GAME_ONLOSE = 6;

	public static Pool pool = null; // 声音
	public static Handler handler = null; // 声明消息处理器引用

	// 用来保存和读取游戏的关数
	SharedPreferences mSharedPreferences = null;

	// 游戏的各个界面
	MyGLSurfaceView mGLSurfaceView = null; // 游戏主界面
	TagSurfaceView mTagSurfaceView = null; // 开始界面
	MenuSurfaceView mMenuSurfaceView = null; // 菜单
	SelectSurfaceView mSelectSurfaceView = null; // 选择
	SetSurfaceView mSetSurfaceView = null; // 设置
	HelpSurfaceView mHelpSurfaceView = null; // 帮助

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		// 初始化用到的常量
		GLConstant.set(this);
		Eye.set();

		// Context.MODE_PRIVATE：新内容覆盖原内容
		// 读取关数
		mSharedPreferences = getSharedPreferences("Setting_himi", MODE_PRIVATE);

		// 读取关数不过不存在设为0关
		// 写进一个关数mSharedPreferences.edit().putString("round", "0").commit();
		int round = Integer
				.parseInt(mSharedPreferences.getString("round", "2"));

		// 防止越界
		if (round >= 0 && round <= 9)
			GLWorld.round = round;
		else
			GLWorld.round = 0;

		// 声音初始化
		pool = new Pool(this);

		mTagSurfaceView = new TagSurfaceView(this);
		setContentView(mTagSurfaceView);

		// 消息处理器
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg)
			{
				super.handleMessage(msg);

				switch (msg.what)
				{
				case MainActivity.GAME_MENU:
					mMenuSurfaceView = new MenuSurfaceView(MainActivity.this);
					setContentView(mMenuSurfaceView);
					break;
				case MainActivity.GAME_SELECT:
					mSelectSurfaceView = new SelectSurfaceView(
							MainActivity.this);
					setContentView(mSelectSurfaceView);
					break;
				case MainActivity.GAME_SET:
					mSetSurfaceView = new SetSurfaceView(MainActivity.this);
					setContentView(mSetSurfaceView);
					break;
				case MainActivity.GAME_HELP:
					mHelpSurfaceView = new HelpSurfaceView(MainActivity.this);
					setContentView(mHelpSurfaceView);
					break;
				case MainActivity.GAME_START:
					mGLSurfaceView = new MyGLSurfaceView(MainActivity.this);
					setContentView(mGLSurfaceView);
					break;
				case MainActivity.GAME_ONWIN:
					onWin();
					break;
				case MainActivity.GAME_ONLOSE:
					onLose();
					break;
				}
			}
		};
	}

	public void onWin()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("游戏胜利");

		builder.setPositiveButton("选择关卡",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton)
					{
						if (GLWorld.round >= 0 && GLWorld.round <= 8)
						{
							GLWorld.round++;
							int round = Integer.parseInt(mSharedPreferences
									.getString("round", "0"));
							if (GLWorld.round > round)
								mSharedPreferences.edit()
										.putString("round", GLWorld.round + "")
										.commit();
						}

						MainActivity.handler
								.sendEmptyMessage(MainActivity.GAME_SELECT);
					}
				});

		builder.setNeutralButton("重新开始", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton)
			{
				MainActivity.handler.sendEmptyMessage(MainActivity.GAME_START);
			}
		});

		if (GLWorld.round >= 0 && GLWorld.round <= 8)
		{
			builder.setNegativeButton("下一关",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton)
						{
							GLWorld.round++;

							int round = Integer.parseInt(mSharedPreferences
									.getString("round", "0"));
							if (GLWorld.round > round)
								mSharedPreferences.edit()
										.putString("round", GLWorld.round + "")
										.commit();

							MainActivity.handler
									.sendEmptyMessage(MainActivity.GAME_START);
						}
					});
		}

		builder.show();
	}

	public void onLose()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("游戏失败");

		builder.setPositiveButton("返回菜单",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton)
					{
						MainActivity.handler
								.sendEmptyMessage(MainActivity.GAME_MENU);
					}
				});

		builder.setNeutralButton("选择关卡", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton)
			{
				MainActivity.handler.sendEmptyMessage(MainActivity.GAME_SELECT);
			}
		});

		builder.setNegativeButton("重新开始",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton)
					{
						MainActivity.handler
								.sendEmptyMessage(MainActivity.GAME_START);
					}
				});

		builder.show();
	}

}
