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
package javax.faces.component;

import javax.faces.FacesException;
import javax.faces.application.FacesMessage;

/**
 * @author Simon Lessard (latest modification by $Author: slessard $)
 * @version $Revision: 696523 $ $Date: 2009-03-14 14:41:20 -0400 (mer., 17 sept. 2008) $
 *
 * @since 2.0
 */
public class UpdateModelException extends FacesException
{
    private FacesMessage _facesMessage;
    
    public UpdateModelException(FacesMessage facesMessage, Throwable cause)
    {
        super(cause);
        
        _facesMessage = facesMessage;
    }
    
    /**
     * @return
     */
    public FacesMessage getFacesMessage()
    {
        return _facesMessage;
    }
}
