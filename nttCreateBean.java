package beanUtils.beanUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import beanUtils.beanUtils.ExcelUtils;
import beanUtils.beanUtils.ExcelUtils.CallBackExcelSingleRow;

public class nttCreateBean {

	static String mdb = "OM";
	static String component = "FN0001";
	static String bid = "1050001";
	static String isData = "da";
	static boolean reqntt = true;
	static boolean resntt = false;
	static String fileName = "服务公司编辑";

	static String nttFilePath = "D:/JavaBean";
	static String fnFilePath = "D:/JavaBean";
	static String packageName = mdb.toLowerCase();
	static String fid = mdb + component;

	public static void main(String[] args) throws Exception {
		File a = new File(nttFilePath);
		File b = new File(fnFilePath);
		if (!a.exists()) {
			a.mkdirs();
		}
		if (!b.exists()) {
			b.mkdirs();
		}
		String path = "D:\\platform-document\\02_系统设计\\20_业务设计\\105_在线商城\\02_接口定义\\0001_服务公司编辑\\接口定义书" + "（" + fileName
				+ "）.xlsx";
		int reqBegin = 11;
		int reqEnd = 16;
		int resBegin = 11;
		int resEnd = 16;
		showBean(path, reqBegin, reqEnd, resBegin, resEnd, 1);
	}

	public static void showBean(String path, final int reqBegin, final int reqEnd, final int resBegin, final int resEnd,
			final int beginRow) throws Exception {
		InputStream input = new FileInputStream(new File(path));
		if (reqntt) {

			ExcelUtils.readExcel(input, ExcelUtils.EXCEL_FORMAT_XLSX, 1, new CallBackExcelSingleRow() {

				ArrayList<NttCreateCondition> conditions = new ArrayList<NttCreateCondition>();

				public void readRow(List<String> rowContent, int rowIndex) {
					if (rowIndex >= reqBegin && rowIndex <= reqEnd) {
						String name = rowContent.get(1).trim();// 全称
						String shortName = rowContent.get(2).trim();// 缩写
						String desc = rowContent.get(3).trim();// 描述
						conditions.add(getCondition(name, shortName, desc));
					}
					if (rowIndex == (reqEnd + 1)) {
						conditions.trimToSize();

						String str = "";
						for (NttCreateCondition condition : conditions) {
							str += ShortDesc(condition);
						}
						for (NttCreateCondition condition : conditions) {
							str += Area(condition);
						}
						REQ();
						reqData(str);

						String check = "";
						for (NttCreateCondition condition : conditions) {
							check += Check(condition);
						}
						FN(check);
					}
				}
			});
		}
		if (resntt) {

			ExcelUtils.readExcel(input, ExcelUtils.EXCEL_FORMAT_XLSX, 1, new CallBackExcelSingleRow() {

				ArrayList<NttCreateCondition> conditions = new ArrayList<NttCreateCondition>();

				public void readRow(List<String> rowContent, int rowIndex) {
					if (rowIndex >= resBegin && rowIndex <= resEnd) {
						String name = rowContent.get(1).trim();// 全称
						String shortName = rowContent.get(2).trim();// 缩写
						String desc = rowContent.get(3).trim();// 描述
						conditions.add(getCondition(name, shortName, desc));
					}
					if (rowIndex == (resEnd + 1)) {
						conditions.trimToSize();

						String str = "";
						for (NttCreateCondition condition : conditions) {
							str += ShortDesc(condition);
						}
						for (NttCreateCondition condition : conditions) {
							str += Area(condition);

							resData(str);
						}
					}
				}
			});
		}
	}

	private static NttCreateCondition getCondition(String name, String shortName, String desc) {
		return new NttCreateCondition(name, shortName, desc);
	}

	// 参数检查
	private static String Check(NttCreateCondition condition) {
		String ss = condition.getName();
		String sss = ss.substring(0, 1).toUpperCase() + ss.substring(1);
		return "//" + condition.getDesc() + "\r\n" + "String " + ss + "=req.getData().get" + sss + "();";
	}

	private static String ShortDesc(NttCreateCondition condition) {
		return "public static final String " + condition.getBigName() + " = \"" + condition.getShortName() + "\";\r\n";
	}

	private static String Area(NttCreateCondition condition) {
		return "/**\r\n*" + condition.getDesc() + "\r\n*/@JsonProperty(value = " + condition.getBigName()
				+ ")\r\nprivate String " + condition.getName() + ";\r\n";
	}

	public static void resData(String str) {
		String result = "package cn.sh.changxing.entity." + packageName
				+ ";\r\nimport org.codehaus.jackson.annotate.JsonProperty;\r\npublic class " + fid
				+ "ResponseBodyData {\r\n" + str;
		result += "}";

		try {
			File file = new File(nttFilePath, fid + "ResponseBodyData.java");
			PrintStream ps = new PrintStream(new FileOutputStream(file));
			ps.println(result);// 往文件里写入字符串
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void res() {
		String result = "package cn.sh.changxing.entity." + packageName
				+ ";\r\nimport org.codehaus.jackson.annotate.JsonProperty;\r\nimport cn.sh.changxing.platform.EntityResponseBody;\r\npublic class "
				+ fid + "ResponseBody extends EntityResponseBody {\r\n@JsonProperty(\"da\")\r\nprivate " + fid
				+ "ResponseBodyData data;\r\npublic void setData(" + fid
				+ "ResponseBodyData data) {\r\nthis.data = data;\r\n}\r\npublic " + fid
				+ "ResponseBodyData getData() {\r\nreturn data;\r\n}\r\n}";

		try {
			File file = new File(nttFilePath, fid + "ResponseBody.java");
			PrintStream ps = new PrintStream(new FileOutputStream(file));
			ps.println(result);// 往文件里写入字符串
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void reqData(String str) {
		String result = "package cn.sh.changxing.entity." + packageName
				+ ";\r\nimport org.codehaus.jackson.annotate.JsonProperty;\r\npublic class " + fid
				+ "RequestBodyData{\r\n" + str;
		result += "}";

		try {
			File file = new File(nttFilePath, fid + "RequestBodyData.java");
			PrintStream ps = new PrintStream(new FileOutputStream(file));
			ps.println(result);// 往文件里写入字符串
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void REQ() {
		String result = "";
		if ("da".equals(isData)) {
			result = "package cn.sh.changxing.entity." + packageName
					+ ";\r\nimport org.codehaus.jackson.annotate.JsonProperty;\r\nimport cn.sh.changxing.platform.EntityRequestBody;\r\npublic class "
					+ fid + "RequestBody extends EntityRequestBody {\r\n@JsonProperty(\"da\")\r\nprivate " + fid
					+ "RequestBodyData data=new " + fid + "RequestBodyData();\r\npublic void setData(" + fid
					+ "RequestBodyData data) {\r\nthis.data = data;\r\n}\r\npublic " + fid
					+ "RequestBodyData getData() {\r\nreturn data;\r\n}\r\n}";
		} else {
			result = "package cn.sh.changxing.entity." + packageName
					+ ";\r\nimport org.codehaus.jackson.annotate.JsonProperty;\r\nimport cn.sh.changxing.platform.EntityRequestBody;\r\npublic class "
					+ fid + "RequestBody extends EntityRequestBody {\r\n@JsonProperty(\"co\")\r\nprivate " + fid
					+ "RequestBodyData condition=new " + fid + "RequestBodyData();\r\npublic void setCondition(" + fid
					+ "RequestBodyData condition) {\r\nthis.condition = condition;\r\n}\r\npublic " + fid
					+ "RequestBodyData getCondition() {\r\nreturn condition;\r\n}\r\n}";
		}

		try {
			File file = new File(nttFilePath, fid + "RequestBody.java");
			PrintStream ps = new PrintStream(new FileOutputStream(file));
			ps.println(result);// 往文件里写入字符串
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void FN(String check) {
		String req = "Entity";
		String res = "Entity";
		String result1 = "";
		if (reqntt) {
			req = fid;
			result1 += "import cn.sh.changxing.entity." + packageName + "." + req + "RequestBody;\r\n";
		} else {
			result1 += "import cn.sh.changxing.platform.EntityRequestBody;\r\n";
		}
		if (resntt) {
			res = fid;
			result1 += "import cn.sh.changxing.entity." + packageName + "." + res + "ResponseBody;\r\n";
		} else {
			result1 += "import cn.sh.changxing.platform.EntityResponseBody;\r\n";
		}
		String result = "package cn.sh.changxing.bs." + packageName + ";\r\n"
				+ "import org.slf4j.Logger;import org.slf4j.LoggerFactory;\r\n"
				+ "import cn.sh.changxing.common.utils.StringUtils;\r\n"
				+ "import cn.sh.changxing.platform.ReturnCodeCategory;\r\n"
				+ "import org.springframework.beans.factory.annotation.Autowired;\r\n"
				+ "import org.springframework.stereotype.Component;\r\n" + "import cn.sh.changxing.mdb." + packageName
				+ ".MDBTemplate" + mdb + ";\r\n" + result1
				+ "import cn.sh.changxing.platform.service.provide.EntityFunctionProvide;\r\n"
				+ "import cn.sh.changxing.platform.service.provide.ProvideException;\r\n" + "/**\r\n" + "*\r\n" + "* "
				+ fileName + "\r\n" + "* @author niushunyuan\r\n" + "*/\r\n" + "@Component(\"" + component + "\")\r\n"
				+ "public class " + fid + " extends EntityFunctionProvide<" + req + "RequestBody, " + res
				+ "ResponseBody>{\r\n" + "private static final Logger LOG=LoggerFactory.getLogger(" + fid
				+ ".class);\r\n" + "@Autowired private MDBTemplate" + mdb + " mdbTemplate" + mdb + ";\r\n"
				+ "private static final String BUSINESS_FUNCTION_ID =\"" + bid + "\";\r\n" + "@Override\r\n"
				+ " protected " + res + "ResponseBody onRequestBody(" + req
				+ "RequestBody req) throws ProvideException {\r\n" + "LOG.info(\"" + fileName + "开始\");\r\n" + "" + res
				+ "ResponseBody res=new " + res + "ResponseBody();\r\n"
				+ "res.setSerialNumber(req.getCommon().getSerialNumber());\r\n" + "" + check + "\r\n"
				+ "if (StringUtils.isNullOrSpace(\"\")) {\r\n"
				+ "addErrorCode(res, BUSINESS_FUNCTION_ID,\"\", \"\");\r\n" + "}\r\n"
				+ "//setFailureReturnCode(res, ReturnCodeCategory.FAILURE_FUNCTION_DATE_OPERATION, BUSINESS_FUNCTION_ID,\");\r\nreturn res;}}";

		try {
			File file = new File(fnFilePath, fid + ".java");
			PrintStream ps = new PrintStream(new FileOutputStream(file));
			ps.println(result);// 往文件里写入字符串
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

class NttCreateCondition {
	private String name = "";
	private String shortName = "";
	private String desc = "";

	public NttCreateCondition(String name, String shortName, String desc) {
		this.name = name;
		this.shortName = shortName;
		this.desc = desc;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getBigName() {
		String bigName = name;
		char[] chars = bigName.toCharArray();
		StringBuilder sBuild = new StringBuilder();
		for (char index : chars) {
			if (Character.isUpperCase(index)) {
				sBuild.append("_");
			}
			sBuild.append(index);
		}
		return sBuild.toString().toUpperCase();
	}
}
