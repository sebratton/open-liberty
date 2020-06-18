-include= ~${workspace}/cnf/resources/bnd/feature.props
symbolicName=io.openliberty.restHandler-2.0
visibility=protected
IBM-App-ForceRestart: uninstall, install
IBM-SPI-Package: com.ibm.wsspi.rest.handler; type="ibm-spi", \
 com.ibm.wsspi.rest.handler.helper; type="ibm-spi"
-features=com.ibm.websphere.appserver.ssl-1.0, \
 com.ibm.websphere.appserver.adminSecurity-1.0, \
 com.ibm.wsspi.appserver.webBundleSecurity-1.0, \
 com.ibm.websphere.appserver.servlet-5.0, \
 com.ibm.websphere.appserver.httptransport-1.0, \
 com.ibm.websphere.appserver.json-1.0, \
 com.ibm.wsspi.appserver.webBundle-1.0
-bundles=com.ibm.ws.org.joda.time.1.6.2, \
 com.ibm.websphere.jsonsupport, \
 com.ibm.ws.rest.handler.jakarta, \
 com.ibm.websphere.rest.handler
-jars=com.ibm.websphere.appserver.spi.restHandler; location:=dev/spi/ibm/
-files=dev/spi/ibm/javadoc/com.ibm.websphere.appserver.spi.restHandler_2.0-javadoc.zip
kind=noship
edition=full
