package com.zzm.chinesesort;

import java.util.Map;
import org.elasticsearch.index.fielddata.ScriptDocValues;
import org.elasticsearch.script.AbstractSearchScript;
import org.elasticsearch.script.ExecutableScript;
import org.elasticsearch.script.NativeScriptFactory;
import net.sourceforge.pinyin4j.PinyinHelper;

/**
 * Script工厂
 * 
 * @author Steven
 *
 */
public class ScriptFactory implements NativeScriptFactory {
	@Override
	public boolean needsScores() {
		return false;
	}

	@Override
	public ExecutableScript newScript(Map<String, Object> params) {
		return new TransScript(params);
	}

	/**
	 * 替换原有字符串的比较脚本
	 * @author Steven
	 *
	 */
	protected class TransScript extends AbstractSearchScript {

		// key为需要转换的字段名
		private String key;

		/**
		 * 初始化脚本
		 * @param params 导入key，key为需要排序的字段名
		 */
		public TransScript(Map<String, Object> params) {
			this.key = (String) params.get("key");
		}

		@Override
		public void setNextVar(String name, Object value) {
			super.setNextVar(name, value);
		}

		@Override
		public Object unwrap(Object value) {
			return super.unwrap(value);
		}

		// 转换方法
		@Override
		public Object run() {
			StringBuffer sb = new StringBuffer();
			String text = ((ScriptDocValues.Strings) doc().get(this.key))
					.getValue();
			for (int i = 0; i < text.length(); i++) {
				char c = text.charAt(i);
				Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
				// 判断是否为中文
				if (Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS == ub) {
					String[] py = PinyinHelper.toHanyuPinyinStringArray(c);
					if (py.length > 0) {
						sb.append(py[0]);
					}
				} else {
					sb.append(c);
				}
			}
			return sb.toString();
		}
	}
}