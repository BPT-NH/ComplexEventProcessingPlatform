/*******************************************************************************
 * Signavio Core Components
 * Copyright (C) 2012  Signavio GmbH
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
/**
 * 
 */
package com.signavio.platform.core.impl;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import javax.servlet.ServletContext;

import com.signavio.platform.core.PlatformProperties;
import com.signavio.platform.exceptions.InitializationException;

/**
 * Read the properties from the web.xml file
 * @author Bjoern Wagner
 *
 */
public class FsPlatformPropertiesImpl implements PlatformProperties {
	private final String serverName;
	private final String platformUri;
	private final String explorerUri;
	private final String editorUri;
	private final String libsUri;
	private final String supportedBrowserEditor;
	
	private final String rootDirectoryPath;
	

	public FsPlatformPropertiesImpl(ServletContext context) {
		supportedBrowserEditor = context.getInitParameter("supportedBrowserEditor");
		
		Properties props = new Properties();
		try {
			props.load(this.getClass().getClassLoader().getResourceAsStream("configuration.properties"));
		} catch (IOException e) {
			throw new InitializationException(e);
		}
		
		String tempRootDirectoryPath = props.getProperty("fileSystemRootDirectory");
		System.out.println("ROOT: " +tempRootDirectoryPath );
		if (tempRootDirectoryPath.endsWith(File.separator)) {
			rootDirectoryPath = tempRootDirectoryPath.substring(0, tempRootDirectoryPath.length()-1);
		} else {
			rootDirectoryPath = tempRootDirectoryPath;
		}
		
		serverName = props.getProperty("host");
		platformUri = context.getContextPath() + "/p";
		explorerUri = context.getContextPath() + "/explorer";
		editorUri = context.getContextPath() + "/editor";
		libsUri = context.getContextPath() + "/libs";
	}
	
	/* (non-Javadoc)
	 * @see com.signavio.platform.core.impl.PlatformProperties#getServerName()
	 */
	public String getServerName() {
		return serverName;
	}
	/* (non-Javadoc)
	 * @see com.signavio.platform.core.impl.PlatformProperties#getPlatformUri()
	 */
	public String getPlatformUri() {
		return platformUri;
	}
	/* (non-Javadoc)
	 * @see com.signavio.platform.core.impl.PlatformProperties#getExplorerUri()
	 */
	public String getExplorerUri() {
		return explorerUri;
	}
	/* (non-Javadoc)
	 * @see com.signavio.platform.core.impl.PlatformProperties#getEditorUri()
	 */
	public String getEditorUri() {
		return editorUri;
	}
	/* (non-Javadoc)
	 * @see com.signavio.platform.core.impl.PlatformProperties#getLibsUri()
	 */
	public String getLibsUri() {
		return libsUri;
	}	
	/* (non-Javadoc)
	 * @see com.signavio.platform.core.impl.PlatformProperties#getSupportedBrowserEditorRegExp()
	 */
	public String getSupportedBrowserEditorRegExp() {
		return supportedBrowserEditor;
	}

	public Set<String> getAdmins() {
		return new HashSet<String>(0);
	}
	
	public String getRootDirectoryPath() {
		return rootDirectoryPath;
	}
}
