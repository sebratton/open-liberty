/*******************************************************************************
 * Copyright (c) 2020 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package test.wab1;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.security.Principal;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.wsspi.rest.handler.RESTHandlerContainer;
import com.ibm.wsspi.rest.handler.RESTRequest;
import com.ibm.wsspi.rest.handler.RESTResponse;
import com.ibm.wsspi.rest.handler.helper.RESTHandlerInternalError;

@WebServlet("/servlet")
public class Servlet1 extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.http.HttpServlet#service(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String wabServiceResponse = "FAIL";
        try {
            mockRHC.handleRequest(mockRReq, mockRRep);
        } catch (RESTHandlerInternalError rhie) {
            //Expected
            wabServiceResponse = "SUCCESS";
        }
        response.getOutputStream().println(wabServiceResponse);
    }

    /********************************************************************************
     * create mocks to allow testing the protected feature loading.
     ********************************************************************************/
    RESTHandlerContainer mockRHC = new RESTHandlerContainer() {

        @Override
        public boolean handleRequest(RESTRequest request, RESTResponse response) throws IOException {
            throw new RESTHandlerInternalError("Test of restHandler type loads");
        }

        @Override
        public Iterator<String> registeredKeys() {
            return null;
        }

    };

    RESTRequest mockRReq = new RESTRequest() {

        @Override
        public Reader getInput() throws IOException {
            return null;
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return null;
        }

        @Override
        public String getHeader(String key) {
            return null;
        }

        @Override
        public String getMethod() {
            return null;
        }

        @Override
        public String getCompleteURL() {
            return null;
        }

        @Override
        public String getURL() {
            return null;
        }

        @Override
        public String getURI() {
            return null;
        }

        @Override
        public String getContextPath() {
            return null;
        }

        @Override
        public String getPath() {
            return null;
        }

        @Override
        public String getQueryString() {
            return null;
        }

        @Override
        public String getParameter(String name) {
            return null;
        }

        @Override
        public String[] getParameterValues(String name) {
            return null;
        }

        @Override
        public Map<String, String[]> getParameterMap() {
            return null;
        }

        @Override
        public Principal getUserPrincipal() {
            return null;
        }

        @Override
        public boolean isUserInRole(String role) {
            return false;
        }

        @Override
        public String getPathVariable(String variable) {
            return null;
        }

        @Override
        public Locale getLocale() {
            return null;
        }

        @Override
        public Enumeration<Locale> getLocales() {
            return null;
        }

        @Override
        public String getRemoteAddr() {
            return null;
        }

        @Override
        public String getRemoteHost() {
            return null;
        }

        @Override
        public int getRemotePort() {
            return 0;
        }

        @Override
        public InputStream getPart(String partName) throws IOException {
            return null;
        }

        @Override
        public boolean isMultiPartRequest() {
            return false;
        }

        @Override
        public String getContentType() {
            return null;
        }

        @Override
        public String getSessionId() {
            return null;
        }
    };

    RESTResponse mockRRep = new RESTResponse() {

        @Override
        public Writer getWriter() throws IOException {
            return null;
        }

        @Override
        public OutputStream getOutputStream() throws IOException {
            return null;
        }

        @Override
        public void setResponseHeader(String key, String value) {
            //method stub
        }

        @Override
        public void addResponseHeader(String key, String value) {
            // method stub

        }

        @Override
        public void setStatus(int statusCode) {
            // method stub
        }

        @Override
        public void sendError(int statusCode) throws IOException {
            // method stub
        }

        @Override
        public void sendError(int statusCode, String msg) throws IOException {
            // TODO method stub
        }

        @Override
        public void setContentType(String contentType) {
            // method stub
        }

        @Override
        public void setContentLength(int len) {
            //  method stub
        }

        @Override
        public void setCharacterEncoding(String charset) {
            //  method stub

        }

        @Override
        public int getStatus() {
            //  method stub
            return 0;
        }

        @Override
        public void setRequiredRoles(Set<String> requiredRoles) {
            // method stub
        }

        @Override
        public Set<String> getRequiredRoles() {
            // method stub
            return null;
        }
    };
}
