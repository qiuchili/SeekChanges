/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.myfaces.application;

import java.util.Map;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.apache.myfaces.buildtools.maven2.plugin.builder.annotation.JSFWebConfigParam;
import org.apache.myfaces.resource.ClassLoaderResourceLoader;
import org.apache.myfaces.resource.ExternalContextResourceLoader;
import org.apache.myfaces.resource.ResourceLoader;
import org.apache.myfaces.shared_impl.application.FacesServletMapping;

/**
 * A ResourceHandlerSupport implementation for use with standard Java Servlet engines,
 * ie an engine that supports javax.servlet, and uses a standard web.xml file.
 * 
 * @author Leonardo Uribe (latest modification by $Author: bommel $)
 * @version $Revision: 915763 $ $Date: 2010-02-24 07:24:11 -0500 (Wed, 24 Feb 2010) $
 */
public class DefaultResourceHandlerSupport implements ResourceHandlerSupport
{

    /**
     * Set the max time in miliseconds set on the "Expires" header for a resource.
     * (default to one week in miliseconds or 604800000) 
     */
    @JSFWebConfigParam(since="2.0", defaultValue="604800000")
    public static final String RESOURCE_MAX_TIME_EXPIRES = "org.apache.myfaces.RESOURCE_MAX_TIME_EXPIRES";

    /**
     * Identifies the FacesServlet mapping in the current request map.
     */
    private static final String CACHED_SERVLET_MAPPING =
        DefaultResourceHandlerSupport.class.getName() + ".CACHED_SERVLET_MAPPING";
    
    private ResourceLoader[] _resourceLoaders;
    
    private Long _startupTime;
    
    private Long _maxTimeExpires;
        
    public DefaultResourceHandlerSupport()
    {
        _startupTime = System.currentTimeMillis();
    }

    public String calculateResourceBasePath(FacesContext facesContext)
    {        
        FacesServletMapping mapping = getFacesServletMapping(facesContext);
        ExternalContext externalContext = facesContext.getExternalContext();      
        
        if (mapping != null)
        {
            String resourceBasePath = null;
            if (mapping.isExtensionMapping())
            {
                // Mapping using a suffix. In this case we have to strip 
                // the suffix. If we have a url like:
                // http://localhost:8080/testjsf20/javax.faces.resource/imagen.jpg.jsf?ln=dojo
                // 
                // The servlet path is /javax.faces.resource/imagen.jpg.jsf
                //
                // For obtain the resource name we have to remove the .jsf suffix and 
                // the prefix ResourceHandler.RESOURCE_IDENTIFIER
                resourceBasePath = externalContext.getRequestServletPath();
                int stripPoint = resourceBasePath.lastIndexOf('.');
                if (stripPoint > 0)
                {
                    resourceBasePath = resourceBasePath.substring(0, stripPoint);
                }
            }
            else
            {
                // Mapping using prefix. In this case we have to strip 
                // the prefix used for mapping. If we have a url like:
                // http://localhost:8080/testjsf20/faces/javax.faces.resource/imagen.jpg?ln=dojo
                //
                // The servlet path is /faces
                // and the path info is /javax.faces.resource/imagen.jpg
                //
                // For obtain the resource name we have to remove the /faces prefix and 
                // then the prefix ResourceHandler.RESOURCE_IDENTIFIER
                resourceBasePath = externalContext.getRequestPathInfo();
            }
            return resourceBasePath;            
        }
        else
        {
            //If no mapping is detected, just return the
            //information follows the servlet path but before
            //the query string
            return externalContext.getRequestPathInfo();
        }
    }

    public ResourceLoader[] getResourceLoaders()
    {
        if (_resourceLoaders == null)
        {
            //The ExternalContextResourceLoader has precedence over
            //ClassLoaderResourceLoader, so it goes first.
            _resourceLoaders = new ResourceLoader[] {
                    new ExternalContextResourceLoader("/resources"),
                    new ClassLoaderResourceLoader("META-INF/resources")
            };
        }
        return _resourceLoaders;
    }
    
    public boolean isExtensionMapping()
    {
        FacesServletMapping mapping = getFacesServletMapping(
                FacesContext.getCurrentInstance());
        if (mapping != null)
        {
            if (mapping.isExtensionMapping())
            {
                return true;
            }
        }
        return false;
    }
    
    public String getMapping()
    {
        FacesServletMapping mapping = getFacesServletMapping(
                FacesContext.getCurrentInstance());
        if (mapping != null)
        {
            if (mapping.isExtensionMapping())
            {
                return mapping.getExtension();
            }
            else
            {
                return mapping.getPrefix();
            }
        }
        return "";
    }

    /**
     * Read the web.xml file that is in the classpath and parse its internals to
     * figure out how the FacesServlet is mapped for the current webapp.
     */
    protected FacesServletMapping getFacesServletMapping(FacesContext context)
    {
        Map<Object, Object> attributes = context.getAttributes();

        // Has the mapping already been determined during this request?
        FacesServletMapping mapping = (FacesServletMapping) attributes.get(CACHED_SERVLET_MAPPING);
        if (mapping == null)
        {
            ExternalContext externalContext = context.getExternalContext();
            mapping = calculateFacesServletMapping(externalContext.getRequestServletPath(),
                    externalContext.getRequestPathInfo());

            attributes.put(CACHED_SERVLET_MAPPING, mapping);
        }
        return mapping;
    }

    /**
     * Determines the mapping of the FacesServlet in the web.xml configuration
     * file. However, there is no need to actually parse this configuration file
     * as runtime information is sufficient.
     *
     * @param servletPath The servletPath of the current request
     * @param pathInfo    The pathInfo of the current request
     * @return the mapping of the FacesServlet in the web.xml configuration file
     */
    protected static FacesServletMapping calculateFacesServletMapping(
        String servletPath, String pathInfo)
    {
        if (pathInfo != null)
        {
            // If there is a "extra path", it's definitely no extension mapping.
            // Now we just have to determine the path which has been specified
            // in the url-pattern, but that's easy as it's the same as the
            // current servletPath. It doesn't even matter if "/*" has been used
            // as in this case the servletPath is just an empty string according
            // to the Servlet Specification (SRV 4.4).
            return FacesServletMapping.createPrefixMapping(servletPath);
        }
        else
        {
            // In the case of extension mapping, no "extra path" is available.
            // Still it's possible that prefix-based mapping has been used.
            // Actually, if there was an exact match no "extra path"
            // is available (e.g. if the url-pattern is "/faces/*"
            // and the request-uri is "/context/faces").
            int slashPos = servletPath.lastIndexOf('/');
            int extensionPos = servletPath.lastIndexOf('.');
            if (extensionPos > -1 && extensionPos > slashPos)
            {
                String extension = servletPath.substring(extensionPos);
                return FacesServletMapping.createExtensionMapping(extension);
            }
            else
            {
                // There is no extension in the given servletPath and therefore
                // we assume that it's an exact match using prefix-based mapping.
                return FacesServletMapping.createPrefixMapping(servletPath);
            }
        }
    }

    public long getStartupTime()
    {
        return _startupTime;
    }
    
    public long getMaxTimeExpires()
    {
        if (_maxTimeExpires == null)
        {
            String time = FacesContext.getCurrentInstance().getExternalContext().getInitParameter(RESOURCE_MAX_TIME_EXPIRES);
            if (time != null && time.length() > 0)
            {
                try
                {
                    _maxTimeExpires = Long.parseLong(time);
                }
                catch (NumberFormatException e)
                {
                    _maxTimeExpires = 604800000L;
                }
            }
            else
            {
                _maxTimeExpires = 604800000L;
            }
        }
        return _maxTimeExpires;
    }
}
