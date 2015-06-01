package de.fhdo.terminologie.ws.search;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * This class was generated by Apache CXF 3.0.3
 * 2015-05-13T09:53:17.068+02:00
 * Generated source version: 3.0.3
 * 
 */
@WebServiceClient(name = "Search", 
                  wsdlLocation = "http://epdis.i4mi.bfh.ch:8080/TermServer/Search?wsdl",
                  targetNamespace = "http://search.ws.terminologie.fhdo.de/") 
public class Search_Service extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("http://search.ws.terminologie.fhdo.de/", "Search");
    public final static QName SearchPort = new QName("http://search.ws.terminologie.fhdo.de/", "SearchPort");
    static {
        URL url = null;
        try {
            url = new URL("http://epdis.i4mi.bfh.ch:8080/TermServer/Search?wsdl");
        } catch (MalformedURLException e) {
            java.util.logging.Logger.getLogger(Search_Service.class.getName())
                .log(java.util.logging.Level.INFO, 
                     "Can not initialize the default wsdl from {0}", "http://epdis.i4mi.bfh.ch:8080/TermServer/Search?wsdl");
        }
        WSDL_LOCATION = url;
    }

    public Search_Service(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public Search_Service(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public Search_Service() {
        super(WSDL_LOCATION, SERVICE);
    }
    
    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public Search_Service(WebServiceFeature ... features) {
        super(WSDL_LOCATION, SERVICE, features);
    }

    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public Search_Service(URL wsdlLocation, WebServiceFeature ... features) {
        super(wsdlLocation, SERVICE, features);
    }

    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public Search_Service(URL wsdlLocation, QName serviceName, WebServiceFeature ... features) {
        super(wsdlLocation, serviceName, features);
    }    

    /**
     *
     * @return
     *     returns Search
     */
    @WebEndpoint(name = "SearchPort")
    public Search getSearchPort() {
        return super.getPort(SearchPort, Search.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns Search
     */
    @WebEndpoint(name = "SearchPort")
    public Search getSearchPort(WebServiceFeature... features) {
        return super.getPort(SearchPort, Search.class, features);
    }

}