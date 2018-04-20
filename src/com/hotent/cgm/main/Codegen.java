
package com.hotent.cgm.main;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.hotent.cgm.db.IDbHelper;
import com.hotent.cgm.exception.CodegenException;
import com.hotent.cgm.model.ConfigModel;
import com.hotent.cgm.model.ConfigModel.Files;
import com.hotent.cgm.model.ConfigModel.GenAll;
import com.hotent.cgm.model.ConfigModel.Table;
import com.hotent.cgm.model.ConfigModel.Templates;
import com.hotent.cgm.model.TableModel;
import com.hotent.cgm.util.FileHelper;
import com.hotent.cgm.util.StringUtil;
import com.hotent.cgm.util.XmlUtil;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class Codegen {
	
	private static String rootPath;
	
	public static void setRootPath(String path) {
		rootPath = path;
	}
	
	private static String getRootPath() {
		if (null == rootPath || rootPath.isEmpty()) {
			rootPath = new File("").getAbsolutePath();
		}
		if (!rootPath.endsWith(File.separator))
			rootPath += File.separator;
		return rootPath;
	}
	
	private static String getXmlPath() throws CodegenException {
		String configXml = getRootPath() + "codegenconfig.xml";
		File file = new File(configXml);
		if (!file.exists()) { throw new CodegenException("请确认配置文件：" + configXml + "是否存在!"); }
		return configXml;
	}
	
	private static String getXsdPath() throws CodegenException {
		String path = getRootPath() + "codegen.xsd";
		File file = new File(path);
		if (!file.exists()) { throw new CodegenException("schema文件" + path + "不存在"); }
		return path;
	}
	
	private static String getPropertiesPath() throws CodegenException {
		String path = getRootPath() + "codegen.properties";
		File file = new File(path);
		if (!file.exists()) { throw new CodegenException("代码生成配置文件" + path + "不存在"); }
		return path;
	}
	
	/**
	 * 读取XML配置
	 * 
	 * @param xmlFile
	 * @return
	 * @throws CodegenException
	 */
	@SuppressWarnings("rawtypes")
	public ConfigModel loadXml() throws Exception {
		// xsd文件路径
		String xsdPath = getXsdPath();
		
		String xmlPath = getXmlPath();
		
		// 验证XML
		String result = XmlUtil.validXmlBySchema(xsdPath, xmlPath);
		
		if (!result.equals("")) { throw new CodegenException("XML文件通过XSD文件校验失败:" + result); }
		// 验证通过， 开始读取XML
		ConfigModel cm = new ConfigModel();
		
		SAXReader saxReader = new SAXReader();
		Document document = null;
		try {
			document = saxReader.read(new File(xmlPath));
		} catch (DocumentException e) {
			throw new CodegenException("读取XML出错!", e);
		}
		Element root = document.getRootElement();
		
		// 从XML中读取variables
		Element variablesEl = root.element("variables");
		for (Iterator j = variablesEl.elementIterator("variable"); j.hasNext();) {
			Element variableEl = (Element) j.next();
			String name = variableEl.attributeValue("name");
			String value = variableEl.attributeValue("value");
			cm.getVariables().put(name, value);
		}
		
		// 从XML中读取templates
		Element templatesEl = root.element("templates");
		// String basepath = templatesEl.attributeValue("basepath");
		Properties prop = new Properties();
		
		String propath = getPropertiesPath();
		
		InputStream in = new BufferedInputStream(new FileInputStream(propath));
		prop.load(in);
		String charset = prop.getProperty("charset");
		String dbHelperClass = prop.getProperty("dbHelperClass");
		String url = prop.getProperty("url");
		String username = prop.getProperty("username");
		String password = prop.getProperty("password");
		String system = prop.getProperty("system");
		cm.getVariables().put("system", system);
		cm.setCharset(charset);
		cm.setDatabase(cm.new Database(dbHelperClass, url, username, password));
		
		String basepath = getRootPath() + "template";
		Templates templates = cm.new Templates(basepath);
		cm.setTemplates(templates);
		for (Iterator j = templatesEl.elementIterator("template"); j.hasNext();) {
			Element templateEl = (Element) j.next();
			String id = templateEl.attributeValue("id");
			String path = templateEl.attributeValue("path");
			templates.getTemplate().put(id, path);
		}
		
		// 从xml读取文件模版
		Element filesEl = root.element("files");
		if (filesEl != null) {
			Files files = cm.new Files();
			cm.setFiles(files);
			
			String baseDir = filesEl.attributeValue("baseDir");
			files.setBaseDir(baseDir);
			for (Iterator j = filesEl.elementIterator("file"); j.hasNext();) {
				
				Element fileEl = (Element) j.next();
				String refTemplate = fileEl.attributeValue("refTemplate");
				String filename = fileEl.attributeValue("filename");
				String dir = StringUtil.replaceVariable(fileEl.attributeValue("dir"), system);
				
				String template = templates.getTemplate().get(refTemplate);
				if (template == null)
					throw new CodegenException("没有找到" + refTemplate + "对应的模版!");
				
				String sub = fileEl.attributeValue("sub");
				String override = fileEl.attributeValue("override");
				boolean isOverride = false;
				if (StringUtil.isNotEmpty(override) && override.equals("true")) {
					isOverride = true;
				}
				String append = fileEl.attributeValue("append");
				if (append != null && append.toLowerCase().equals("true")) {
					String insertTag = fileEl.attributeValue("insertTag");
					String startTag = fileEl.attributeValue("startTag");
					String endTag = fileEl.attributeValue("endTag");
					if (insertTag == null)
						insertTag = "<!--insertbefore-->";
					if (StringUtil.isEmpty(startTag))
						startTag = "";
					if (StringUtil.isEmpty(endTag))
						endTag = "";
					if (sub != null && sub.toLowerCase().equals("true")) {
						files.addFile(template, filename, dir, true, isOverride, true, insertTag, startTag, endTag);
					} else {
						files.addFile(template, filename, dir, false, isOverride, true, insertTag, startTag, endTag);
					}
					
				} else {
					if (sub != null && sub.toLowerCase().equals("true")) {
						files.addFile(template, filename, dir, true, isOverride, false, "", "", "");
					} else {
						files.addFile(template, filename, dir, false, isOverride, false, "", "", "");
					}
				}
			}
			// 从XML中读取table
			for (Iterator i = root.elementIterator("table"); i.hasNext();) {
				Element tableEl = (Element) i.next();
				String tableName = tableEl.attributeValue("tableName");
				
				Table table = cm.new Table(tableName);
				for (Iterator j = tableEl.elementIterator("variable"); j.hasNext();) {
					Element fileEl = (Element) j.next();
					String name = fileEl.attributeValue("name");
					String value = fileEl.attributeValue("value");
					table.getVariable().put(name, value);
					if (StringUtil.isNotEmpty(table.getVariable().get("class"))) {
						String classVar = StringUtil.toFirstLowerCase(table.getVariable().get("class"));
						table.getVariable().put("classVar", classVar);
					}
				}
				// 添加子表
				for (Iterator m = tableEl.elementIterator("subtable"); m.hasNext();) {
					Element subEl = (Element) m.next();
					String tablename = subEl.attributeValue("tablename");
					String foreignKey = subEl.attributeValue("foreignKey");
					Map<String, String> vars = new HashMap<String, String>();
					
					for (Iterator var = subEl.elementIterator("variable"); var.hasNext();) {
						Element varEl = (Element) var.next();
						String name = varEl.attributeValue("name");
						String value = varEl.attributeValue("value");
						vars.put(name, value);
						if (StringUtil.isNotEmpty(vars.get("class"))) {
							String classVar = StringUtil.toFirstLowerCase(vars.get("class"));
							vars.put("classVar", classVar);
						}
					}
					
					// 添加子表变量。
					table.addSubTable(tablename, foreignKey, vars);
				}
				cm.getTables().add(table);
			}
		}
		
		// 从XML中读取genAll
		for (Iterator i = root.elementIterator("genAll"); i.hasNext();) {
			Element tableEl = (Element) i.next();
			String tableNames = tableEl.attributeValue("tableNames");
			GenAll genAll = cm.new GenAll(tableNames);
			cm.setGenAll(genAll);
			for (Iterator j = tableEl.elementIterator("file"); j.hasNext();) {
				Element fileEl = (Element) j.next();
				String refTemplate = fileEl.attributeValue("refTemplate");
				String filename = fileEl.attributeValue("filename");
				String extName = fileEl.attributeValue("extName");
				String dir = fileEl.attributeValue("dir");
				String genMode = fileEl.attributeValue("genMode");
				String template = cm.getTemplates().getTemplate().get(refTemplate);
				if (template == null) { throw new CodegenException("找不到模板： " + refTemplate + "!"); }
				if ("SingleFile".equals(genMode) && filename == null) {
					throw new CodegenException("当SingleFile模式时，必须要填filename!");
				} else if ("MultiFile".equals(genMode)
						&& extName == null) { throw new CodegenException("当MultiFile模式时，必须要填extName!"); }
				GenAll.File file = genAll.new File(template, filename, extName, dir, genMode);
				genAll.getFile().add(file);
				for (Iterator k = fileEl.elementIterator("variable"); k.hasNext();) {
					Element variableEl = (Element) k.next();
					String name = variableEl.attributeValue("name");
					String value = variableEl.attributeValue("value");
					file.getVariable().put(name, value);
				}
			}
		}
		return cm;
	}
	
	/**
	 * 根据配置获取表元数据列表。
	 * 
	 * @param dbHelper
	 * @param configModel
	 * @return
	 * @throws CodegenException
	 */
	private List<TableModel> getTableModelList(IDbHelper dbHelper, ConfigModel configModel) throws CodegenException {
		List<ConfigModel.Table> tables = configModel.getTables();
		List<TableModel> tableModels = new ArrayList<TableModel>();
		for (ConfigModel.Table table : tables) {
			String tbName = table.getTableName();
			// 从数据库中读取的tableModel
			TableModel tableModel = dbHelper.getByTable(tbName);
			// 设置表
			tableModel.setVariables(table.getVariable());
			tableModel.setSub(false);
			// 添加从表
			List<ConfigModel.Table.SubTable> subtables = table.getSubtable();
			for (ConfigModel.Table.SubTable sb : subtables) {
				String tableName = sb.getTableName();
				String foreignKey = sb.getForeignKey();
				Map<String, String> variables = sb.getVars();
				TableModel subTable = dbHelper.getByTable(tableName);
				subTable.setVariables(variables);
				subTable.setSub(true);
				subTable.setForeignKey(foreignKey);
				tableModel.getSubTableList().add(subTable);
				tableModels.add(subTable);
			}
			tableModels.add(tableModel);
		}
		return tableModels;
	}
	
	//
	/**
	 * 根据配置生成。
	 * 
	 * @param configModel
	 * @throws CodegenException
	 */
	private void genTableByConfig(IDbHelper dbHelper, ConfigModel configModel, Configuration cfg)
			throws CodegenException {
		try {
			List<TableModel> tableModels = getTableModelList(dbHelper, configModel);
			
			if (tableModels == null || tableModels.size() == 0) {
				System.out.println("没有指定生成的表!");
				return;
			}
			
			Files files = configModel.getFiles();
			String baseDir = files.getBaseDir();
			
			List<ConfigModel.Files.File> fileList = files.getFiles();
			if (fileList == null || fileList.size() == 0) {
				System.out.println("没有指定生成的文件!");
				return;
			}
			
			System.out.println("*********开始生成**********");
			
			for (TableModel tableModel : tableModels) {
				String tbName = tableModel.getTableName();
				Map<String, String> variables = tableModel.getVariables();
				boolean isSub = tableModel.getSub();
				// 遍历生成文件
				for (ConfigModel.Files.File file : fileList) {
					// 文件名
					String filename = file.getFilename();
					String startTag = file.getStartTag();
					String endTag = file.getEndTag();
					boolean sub = file.isSub();
					boolean override = file.isOverride();
					if (isSub == true && sub == false)
						continue;
					startTag = StringUtil.replaceVariable(startTag, tbName);
					endTag = StringUtil.replaceVariable(endTag, tbName);
					// 生成文件目录
					String dir = file.getDir();
					filename = StringUtil.replaceVariable(filename, variables);
					dir = StringUtil.replaceVariable(dir, variables);
					dir = StringUtil.combilePath(baseDir, dir);
					
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("model", tableModel);
					map.put("vars", configModel.getVariables());
					map.put("date", new Date());
					
					// 文件添加。
					if (file.getAppend()) {
						appendFile(dir, filename, file.getTemplate(), configModel.getCharset(), cfg, map,
								file.getInsertTag(), startTag, endTag);
					} else {
						if (override) {
							genFile(dir, filename, file.getTemplate(), configModel.getCharset(), cfg, map);
						} else {
							File f = new File(dir + File.separator + filename);
							if (!f.exists()) {
								genFile(dir, filename, file.getTemplate(), configModel.getCharset(), cfg, map);
							}
						}
					}
					System.out.println(filename + " 生成成功!");
				}
			}
			System.out.println("*********所有文件生成成功!**********");
		} catch (IOException e) {
			throw new CodegenException(e);
		} catch (TemplateException e) {
			throw new CodegenException("freemarker执行出错!", e);
		}
		
	}
	
	private void getAllTable(IDbHelper dbHelper, ConfigModel configModel, Configuration cfg)
			throws CodegenException, IOException, TemplateException {
		// XML中的genAll配置
		GenAll genAll = configModel.getGenAll();
		if (genAll == null)
			return;
		
		List<String> tableNames = null;
		if (genAll.getTableNames() == null) {
			tableNames = dbHelper.getAllTable();
		} else {
			tableNames = new ArrayList<String>();
			for (String name : genAll.getTableNames().replaceAll(" ", "").split(",")) {
				if (name.length() > 0) {
					tableNames.add(name);
				}
			}
		}
		int size = genAll.getFile().size();
		
		if (size == 0)
			return;
		
		System.out.println("----------------生成多表开始------------------");
		
		for (ConfigModel.GenAll.File file : genAll.getFile()) {
			if ("MultiFile".equals(file.getGenMode())) {
				for (String tableName : tableNames) {
					// 从数据库中读取的tableModel
					TableModel tableModel = dbHelper.getByTable(tableName);
					
					Map<String, Object> map = new HashMap<String, Object>();
					
					map.put("model", tableModel);
					map.put("date", new Date());
					
					genFile(file.getDir(), tableName + "." + file.getExtName(), file.getTemplate(),
							configModel.getCharset(), cfg, map);
					System.out.println(tableName + "." + file.getExtName() + " 生成成功!");
				}
				
			} else if ("SingleFile".equals(file.getGenMode())) {
				List<TableModel> models = new ArrayList<TableModel>();
				for (String tableName : tableNames) {
					// 从数据库中读取的tableModel
					TableModel tableModel = dbHelper.getByTable(tableName);
					models.add(tableModel);
				}
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("models", models);
				map.put("date", new Date());
				genFile(file.getDir(), file.getFilename(), file.getTemplate(), configModel.getCharset(), cfg, map);
				System.out.println(file.getFilename() + " 生成成功!");
			}
		}
		System.out.println("----------------生成多表结束------------------");
		
	}
	
	/**
	 * 生成文件
	 * 
	 * @param fileDir
	 * @param fileName
	 * @param templatePath
	 * @param charset
	 * @param cfg
	 * @param data
	 * @throws IOException
	 * @throws TemplateException
	 */
	private void genFile(String fileDir, String fileName, String templatePath, String charset, Configuration cfg,
			Map data) throws IOException, TemplateException {
		String path = StringUtil.combilePath(fileDir, fileName);
		// 事先备份文件
		// FileHelper.backupFile(path);
		File newFile = new File(fileDir, fileName);
		if (!newFile.exists()) {
			if (!newFile.getParentFile().exists()) {
				newFile.getParentFile().mkdirs();
			}
			newFile.createNewFile();
		}
		Writer writer = new OutputStreamWriter(new FileOutputStream(newFile), charset);
		// 模板文件
		Template template = cfg.getTemplate(templatePath, charset);
		// 生成
		template.process(data, writer);
	}
	
	private void appendFile(String fileDir, String fileName, String templatePath, String charset, Configuration cfg,
			Map data, String insertTag, String startTag, String endTag) throws IOException, TemplateException {
		
		String path = StringUtil.combilePath(fileDir, fileName);
		File newFile = new File(fileDir, fileName);
		
		StringWriter out = new StringWriter();
		Template template = cfg.getTemplate(templatePath, charset);
		template.process(data, out);
		String str = out.toString();
		
		boolean exists = false;
		String content = "";
		if (newFile.exists()) {
			content = FileHelper.readFile(path, charset);
			if (StringUtil.isNotEmpty(startTag) && StringUtil.isNotEmpty(endTag)) {
				if (StringUtil.isExist(content, startTag, endTag)) {
					content = StringUtil.replace(content, startTag, endTag, str);
					exists = true;
				}
			}
		}
		// 已经替换过，跳过此操作。
		if (!exists) {
			if (StringUtil.isNotEmpty(startTag) && StringUtil.isNotEmpty(endTag)) {
				str = startTag.trim() + "\r\n" + str + "\r\n" + endTag.trim();
			}
			if (content.indexOf(insertTag) != -1) {
				String[] aryContent = FileHelper.getBySplit(content, insertTag);
				content = aryContent[0] + str + "\r\n" + insertTag + aryContent[1];
			} else {
				content = content + "\r\n" + str;
			}
		}
		FileHelper.writeFile(newFile, charset, content);
	}
	
	/**
	 * 获取DbHelper。
	 * 
	 * @param configModel
	 * @return
	 * @throws CodegenException
	 */
	private IDbHelper getDbHelper(ConfigModel configModel) throws CodegenException {
		IDbHelper dbHelper = null;
		String dbHelperClass = configModel.getDatabase().getDbHelperClass();
		try {
			dbHelper = (IDbHelper) Class.forName(dbHelperClass).newInstance();
		} catch (InstantiationException e) {
			throw new CodegenException(
					"指定的dbHelperClass：" + dbHelperClass + "无法实例化，可能该类是一个抽象类、接口、数组类、基本类型或 void, 或者该类没有默认构造方法!", e);
		} catch (IllegalAccessException e) {
			throw new CodegenException("指定的dbHelperClass： " + dbHelperClass + "没有默认构造方法或不可访问!", e);
		} catch (ClassNotFoundException e) {
			throw new CodegenException("找不到指定的dbHelperClass:" + dbHelperClass + "!", e);
		}
		dbHelper.setUrl(configModel.getDatabase().getUrl(), configModel.getDatabase().getUsername(),
				configModel.getDatabase().getPassword());
		return dbHelper;
	}
	
	public void execute() throws Exception {
		try {
			String configXml = getRootPath() + "codegenconfig.xml";
			File file = new File(configXml);
			if (!file.exists()) { throw new CodegenException("请确认配置文件：" + configXml + "是否存在!"); }
			ConfigModel configModel = loadXml();
			Configuration cfg = new Configuration();
			cfg.setDirectoryForTemplateLoading(new File(configModel.getTemplates().getBasepath()));
			IDbHelper dbHelper = getDbHelper(configModel);
			// 生成表
			genTableByConfig(dbHelper, configModel, cfg);
			// 生成全部。
			getAllTable(dbHelper, configModel, cfg);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		try {
			Codegen codegen = new Codegen();
			codegen.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
