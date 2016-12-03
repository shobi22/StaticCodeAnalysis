package com.project.staticss.java8;

import java.io.File;




public class FilePropertyName {

	public static String user_home = "C://Users//Public//Documents//CS585staticCode";
	public static String RESOURCE_PATH = user_home + File.separator + "Resources" + File.separator + "res";
	public static final String XML = "xml";
	public static final String PROPERTY = "property";
	public static String SOURCE_ARTIFACT_NAME = "SourceCodeArtefactFile.xml";
	public static final String SOURCE_ARETEFACT_NAME_OLD = "old_version.xml";
	public static final String SOURCE_ARETEFACT_NAME_NEW = "new_version.xml";
	public static final String SOURCE_ARETEFACT_NAME_MODIFIED = "modified_version.xml";

	private static String getXMLFileRootPath(String projectPath) {
		String fileRoot = projectPath + File.separator + FilePropertyName.XML + File.separator;

		return fileRoot;
	}

	public static String getXMLFilePath(String projectPath, String TAG) {
		String path = "";
		path += getXMLFileRootPath(projectPath);
		String fileName = "";
		if (TAG.equals("OLD")) {
			fileName = FilePropertyName.SOURCE_ARETEFACT_NAME_OLD;
		} else if (TAG.equals("NEW")) {
			fileName = FilePropertyName.SOURCE_ARETEFACT_NAME_NEW;
		} else if (TAG.equals("MODIFIED")) {
			fileName = FilePropertyName.SOURCE_ARETEFACT_NAME_MODIFIED;
		} else {
			fileName = FilePropertyName.SOURCE_ARTIFACT_NAME;
		}

		path += fileName;
		return path;
	}

}
