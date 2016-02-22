package com.zzm.chinesesort;

import org.elasticsearch.plugins.Plugin;
import org.elasticsearch.script.ScriptModule;

/**
 * NativeScriptPlugin 插件注册类
 * @author Steven
 */

public class NativeScriptPlugin extends Plugin {

	//插件名称
	@Override
	public String name() {
		return "cchinese_sort";
	}

	//插件描述
	@Override
	public String description() {
		return "a script used to change String to Pinyin";
	}

	//注册插件
	public void onModule(ScriptModule module) {
		module.registerScript("chinese_sort", ScriptFactory.class);
	}
}