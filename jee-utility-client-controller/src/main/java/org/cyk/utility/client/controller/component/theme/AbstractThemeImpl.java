package org.cyk.utility.client.controller.component.theme;

import java.io.Serializable;
import java.util.Collection;

import org.cyk.utility.client.controller.AbstractObject;
import org.cyk.utility.client.controller.Constant;
import org.cyk.utility.client.controller.component.file.File;
import org.cyk.utility.client.controller.component.file.FileImage;
import org.cyk.utility.client.controller.component.file.FileImageBuilder;
import org.cyk.utility.client.controller.component.file.FileImageMap;
import org.cyk.utility.client.controller.component.file.Files;
import org.cyk.utility.client.controller.component.script.Script;
import org.cyk.utility.client.controller.component.script.Scripts;
import org.cyk.utility.client.controller.component.view.ViewMap;
import org.cyk.utility.client.controller.tag.TagLink;
import org.cyk.utility.client.controller.tag.TagLinks;
import org.cyk.utility.client.controller.tag.TagMap;
import org.cyk.utility.client.controller.tag.TagMeta;
import org.cyk.utility.client.controller.tag.TagMetas;
import org.cyk.utility.client.controller.tag.TagScript;
import org.cyk.utility.client.controller.tag.TagScripts;
import org.cyk.utility.file.FileHelper;
import org.cyk.utility.string.StringHelper;

public abstract class AbstractThemeImpl extends AbstractObject implements Theme,Serializable {
	private static final long serialVersionUID = 1L;

	private TagMetas tagMetas;
	private TagLinks tagLinks;
	private TagScripts tagScripts;
	private Files cascadeStyleSheetFiles;
	private Files javaScriptFiles;
	private FileImageMap imageMap;
	private ViewMap viewMap;
	private ThemeTemplate template;
	private TagMap tagMap;
	private Scripts scripts;
	private Object request;
	
	@Override
	public Theme build() {
		setIdentifier(__getIdentifier__());
		setTemplate(__inject__(ThemeTemplate.class));
		getTemplate().setIdentifier(__getTemplateIdentifier__());
		
		String logoFileName = __getConfigurationParameterValue__(Constant.CONTEXT_PARAMETER_NAME_THEME_LOGO_FILE_NAME,null);
		if(__inject__(StringHelper.class).isBlank(logoFileName))
			logoFileName = __inject__(FileHelper.class).concatenateNameAndExtension(__getConfigurationParameterValue__(Constant.CONTEXT_PARAMETER_NAME_THEME_LOGO_FILE_NAME_PREFIX,"logo")
					, __getConfigurationParameterValue__(Constant.CONTEXT_PARAMETER_NAME_THEME_LOGO_FILE_NAME_EXTENSION,"png"));
		FileImageBuilder fileImageBuilder = __inject__(FileImageBuilder.class);
		
		fileImageBuilder.setRequest(getRequest());
		fileImageBuilder.setResourcesFolderName(__getConfigurationParameterValue__(Constant.CONTEXT_PARAMETER_NAME_THEME_LOGO_FILE_RESOURCES_FOLDER,null));
		fileImageBuilder.getFile(Boolean.TRUE)
		.setValuePath(__getConfigurationParameterValue__(Constant.CONTEXT_PARAMETER_NAME_THEME_LOGO_FILE_FOLDER,"image"))
		.setValueName(logoFileName);
		setLogo(fileImageBuilder.execute().getOutput());
		
		return this;
	}
	
	protected abstract String __getIdentifier__();
	protected abstract String __getTemplateIdentifier__();
	protected abstract Object __getContext__(Object request);
	
	@Override
	public Object getRequest() {
		return request;
	}
	
	@Override
	public Theme setRequest(Object request) {
		this.request = request;
		return this;
	}
	
	@Override
	public ViewMap getViewMap() {
		return viewMap;
	}
	
	@Override
	public ViewMap getViewMap(Boolean injectIfNull) {
		return (ViewMap) __getInjectIfNull__(FIELD_VIEW_MAP, injectIfNull);
	}

	@Override
	public Theme setViewMap(ViewMap viewMap) {
		this.viewMap = viewMap;
		return this;
	}

	@Override
	public Theme mapViews(Object...objects) {
		getViewMap(Boolean.TRUE).set(objects);
		return this;
	}
	
	@Override
	public ThemeTemplate getTemplate() {
		return template;
	}

	@Override
	public Theme setTemplate(ThemeTemplate template) {
		this.template = template;
		return this;
	}
	
	@Override
	public Files getCascadeStyleSheetFiles() {
		return cascadeStyleSheetFiles;
	}
	
	@Override
	public Files getCascadeStyleSheetFiles(Boolean injectIfNull) {
		return (Files) __getInjectIfNull__(FIELD_CASCADE_STYLE_SHEET_FILES, injectIfNull);
	}
	
	@Override
	public Theme setCascadeStyleSheetFiles(Files cascadeStyleSheetFiles) {
		this.cascadeStyleSheetFiles = cascadeStyleSheetFiles;
		return this;
	}
	
	@Override
	public Theme addCascadeStyleSheetFiles(Collection<File> cascadeStyleSheetFiles) {
		getCascadeStyleSheetFiles(Boolean.TRUE).add(cascadeStyleSheetFiles);
		return this;
	}
	
	@Override
	public Theme addCascadeStyleSheetFiles(File... cascadeStyleSheetFiles) {
		getCascadeStyleSheetFiles(Boolean.TRUE).add(cascadeStyleSheetFiles);
		return this;
	}
	
	@Override
	public Files getJavaScriptFiles() {
		return javaScriptFiles;
	}
	
	@Override
	public Files getJavaScriptFiles(Boolean injectIfNull) {
		return (Files) __getInjectIfNull__(FIELD_JAVA_SCRIPT_FILES, injectIfNull);
	}
	
	@Override
	public Theme setJavaScriptFiles(Files javaScriptFiles) {
		this.javaScriptFiles = javaScriptFiles;
		return this;
	}
	
	@Override
	public Theme addJavaScriptFiles(Collection<File> javaScriptFiles) {
		getJavaScriptFiles(Boolean.TRUE).add(javaScriptFiles);
		return this;
	}
	
	@Override
	public Theme addJavaScriptFiles(File... objects) {
		getJavaScriptFiles(Boolean.TRUE).add(javaScriptFiles);
		return this;
	}
	
	@Override
	public FileImageMap getImageMap() {
		return imageMap;
	}
	
	@Override
	public FileImageMap getImageMap(Boolean injectIfNull) {
		return (FileImageMap) __getInjectIfNull__(FIELD_IMAGE_MAP, injectIfNull);
	}
	
	@Override
	public Theme setImageMap(FileImageMap imageMap) {
		this.imageMap = imageMap;
		return this;
	}
	
	@Override
	public Theme mapImages(Object... objects) {
		getImageMap(Boolean.TRUE).set(objects);
		return this;
	}
	
	@Override
	public FileImage getIcon() {
		FileImageMap map = getImageMap();
		return map == null ? null : map.get(IMAGE_ICON);
	}
	
	@Override
	public FileImage getIcon(Boolean injectIfNull) {
		FileImageMap map = getImageMap(injectIfNull);
		return map == null ? null : map.get(IMAGE_ICON,injectIfNull);
	}
	
	@Override
	public Theme setIcon(FileImage icon) {
		getImageMap(Boolean.TRUE).set(IMAGE_ICON,icon);
		return this;
	}
	
	@Override
	public FileImage getLogo() {
		FileImageMap map = getImageMap();
		return map == null ? null : map.get(IMAGE_LOGO);
	}
	
	@Override
	public FileImage getLogo(Boolean injectIfNull) {
		FileImageMap map = getImageMap(injectIfNull);
		return map == null ? null : map.get(IMAGE_LOGO,injectIfNull);
	}
	
	@Override
	public Theme setLogo(FileImage logo) {
		getImageMap(Boolean.TRUE).set(IMAGE_LOGO,logo);
		return this;
	}
	
	@Override
	public TagMap getTagMap() {
		return tagMap;
	}
	
	@Override
	public TagMap getTagMap(Boolean injectIfNull) {
		return (TagMap) __getInjectIfNull__(FIELD_TAG_MAP, injectIfNull);
	}
	
	@Override
	public Theme setTagMap(TagMap tagMap) {
		this.tagMap = tagMap;
		return this;
	}
	
	@Override
	public Theme mapTags(Object... keyValues) {
		getTagMap(Boolean.TRUE).set(keyValues);
		return this;
	}
	
	@Override
	public TagLinks getTagLinks() {
		return tagLinks;
	}
	
	@Override
	public TagLinks getTagLinks(Boolean injectIfNull) {
		return (TagLinks) __getInjectIfNull__(FIELD_TAG_LINKS, injectIfNull);
	}
	
	@Override
	public Theme setTagLinks(TagLinks tagLinks) {
		this.tagLinks = tagLinks;
		return this;
	}
	
	@Override
	public Theme addTagLinks(Collection<TagLink> tagLinks) {
		getTagLinks(Boolean.TRUE).add(tagLinks);
		return this;
	}
	
	@Override
	public Theme addTagLinks(TagLink... tagLinks) {
		getTagLinks(Boolean.TRUE).add(tagLinks);
		return this;
	}
	
	@Override
	public TagMetas getTagMetas() {
		return tagMetas;
	}
	
	@Override
	public TagMetas getTagMetas(Boolean injectIfNull) {
		return (TagMetas) __getInjectIfNull__(FIELD_TAG_METAS, injectIfNull);
	}
	
	@Override
	public Theme setTagMetas(TagMetas tagMetas) {
		this.tagMetas = tagMetas;
		return this;
	}
	
	@Override
	public Theme addTagMetas(Collection<TagMeta> tagMetas) {
		getTagMetas(Boolean.TRUE).add(tagMetas);
		return this;
	}
	
	@Override
	public Theme addTagMetas(TagMeta... tagMetas) {
		getTagMetas(Boolean.TRUE).add(tagMetas);
		return this;
	}
	
	@Override
	public TagScripts getTagScripts() {
		return tagScripts;
	}
	
	@Override
	public TagScripts getTagScripts(Boolean injectIfNull) {
		return (TagScripts) __getInjectIfNull__(FIELD_TAG_SCRIPTS, injectIfNull);
	}
	
	@Override
	public Theme setTagScripts(TagScripts tagScripts) {
		this.tagScripts = tagScripts;
		return this;
	}
	
	@Override
	public Theme addTagScripts(Collection<TagScript> tagScripts) {
		getTagScripts(Boolean.TRUE).add(tagScripts);
		return this;
	}
	
	@Override
	public Theme addTagScripts(TagScript... tagScripts) {
		getTagScripts(Boolean.TRUE).add(tagScripts);
		return this;
	}
	
	@Override
	public Scripts getScripts() {
		return scripts;
	}
	
	@Override
	public Scripts getScripts(Boolean injectIfNull) {
		return (Scripts) __getInjectIfNull__(FIELD_SCRIPTS, injectIfNull);
	}
	
	@Override
	public Theme setScripts(Scripts scripts) {
		this.scripts = scripts;
		return this;
	}
	
	@Override
	public Theme addScripts(Collection<Script> scripts) {
		getScripts(Boolean.TRUE).add(scripts);
		return this;
	}
	
	@Override
	public Theme addScripts(Script... scripts) {
		getScripts(Boolean.TRUE).add(scripts);
		return this;
	}
	
	/**/
	
	protected String __getConfigurationParameterValue__(String name,String nullValue) {
		Object request = getRequest();
		return Constant.getConfigurationParameterValue(name, __getContext__(request), request, nullValue);
	}
	
	/**/

	public static final String FIELD_SCRIPTS = "scripts";
	public static final String FIELD_TAG_SCRIPTS = "tagScripts";
	public static final String FIELD_TAG_LINKS = "tagLinks";
	public static final String FIELD_TAG_METAS = "tagMetas";
	public static final String FIELD_VIEW_MAP = "viewMap";
	public static final String FIELD_CASCADE_STYLE_SHEET_FILES = "cascadeStyleSheetFiles";
	public static final String FIELD_JAVA_SCRIPT_FILES = "javaScriptFiles";
	public static final String FIELD_IMAGE_MAP = "imageMap";
	public static final String FIELD_TAG_MAP = "tagMap";
	
	private static final String IMAGE_ICON = "icon";
	private static final String IMAGE_LOGO = "logo";
	
}