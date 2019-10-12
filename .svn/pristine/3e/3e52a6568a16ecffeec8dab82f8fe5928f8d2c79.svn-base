package com.xxl.job.executor.test;

import com.alibaba.fastjson.JSON;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration*/
/*@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class ,webEnvironment= SpringBootTest.WebEnvironment.DEFINED_PORT)*/
public class XxlJobExecutorExampleBootApplicationTests {

	@Test
	public void test() {
		Bean a = new Bean();
		a.setGlueSource("123456");
		a.getEngineParams().put("a", "a");
		a.getEngineParams().put("b", "b");

		a.getParams().put("c", "c");
		a.getParams().put("d", "d");
		String source = JSON.toJSONString(a);
		System.out.println(source);
		Bean b = JSON.parseObject(source, Bean.class);
		System.out.println(b);

		System.out.println(JSON.parseObject("a,d,f,g"));
	}

	@Test
	public void testRegex(){
		String str = "";
		String pattern = "$@{*}$@";

		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(str);
		System.out.println(m.matches());
	}

}

class Bean {
	private String glueSource;
	private Map<String, String> engineParams = new HashMap<>();
	private Map<String, String> params = new HashMap<>();

	public String getGlueSource() {
		return glueSource;
	}

	public void setGlueSource(String glueSource) {
		this.glueSource = glueSource;
	}

	public Map<String, String> getEngineParams() {
		return engineParams;
	}

	public void setEngineParams(Map<String, String> engineParams) {
		this.engineParams = engineParams;
	}

	public Map<String, String> getParams() {
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}
}