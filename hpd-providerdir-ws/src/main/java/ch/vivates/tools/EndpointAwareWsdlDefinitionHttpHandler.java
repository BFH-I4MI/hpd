package ch.vivates.tools;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.springframework.util.FileCopyUtils;
import org.springframework.ws.transport.http.HttpTransportConstants;
import org.springframework.ws.wsdl.WsdlDefinition;
import org.springframework.xml.transform.TransformerObjectSupport;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

@SuppressWarnings("restriction")
public class EndpointAwareWsdlDefinitionHttpHandler extends TransformerObjectSupport  implements HttpHandler, InitializingBean {

    private static final String CONTENT_TYPE = "text/xml";

    private WsdlDefinition definition;
    
    private String serviceEndpoint;

    public EndpointAwareWsdlDefinitionHttpHandler() {
    }

    public EndpointAwareWsdlDefinitionHttpHandler(WsdlDefinition definition) {
        this.definition = definition;
    }

    public void setDefinition(WsdlDefinition definition) {
        this.definition = definition;
    }

	public void setServiceEndpoint(String serviceEndpoint) {
		this.serviceEndpoint = serviceEndpoint;
	}

	public void afterPropertiesSet() throws Exception {
        Assert.notNull(definition, "'definition' is required");
    }

    public void handle(HttpExchange httpExchange) throws IOException {
        try {
            if (HttpTransportConstants.METHOD_GET.equals(httpExchange.getRequestMethod())) {
                Headers headers = httpExchange.getResponseHeaders();
                headers.set(HttpTransportConstants.HEADER_CONTENT_TYPE, CONTENT_TYPE);
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                transform(definition.getSource(), new StreamResult(os));
                String oss = new String(os.toByteArray());
                byte[] buf = oss.replace("[[SERVICE_ENDPOINT_PLACEHOLDER]]", serviceEndpoint).getBytes();
                httpExchange.sendResponseHeaders(HttpTransportConstants.STATUS_OK, buf.length);
                FileCopyUtils.copy(buf, httpExchange.getResponseBody());
            }
            else {
                httpExchange.sendResponseHeaders(HttpTransportConstants.STATUS_METHOD_NOT_ALLOWED, -1);
            }
        }
        catch (TransformerException ex) {
            logger.error(ex, ex);
        }
        finally {
            httpExchange.close();
        }
    }
}