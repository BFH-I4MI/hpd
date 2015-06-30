package ch.bfh.i4mi.processors;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

public class ExceptionProcessor implements Processor {

	private static final Logger LOG = LoggerFactory
			.getLogger("PrepareProcessor");

	protected JmsTemplate jmsTemplate;
	
	private String payload;

	public JmsTemplate getJmsTemplate() {
		return jmsTemplate;
	}

	public void setJmsTemplate(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}

	@Override
	public void process(Exchange exchange) throws Exception {
		LOG.info("Sending exception to queue....");
		payload = exchange.getIn().getBody(String.class);		
		jmsTemplate.send(new MessageCreator() {

			@Override
			public Message createMessage(Session session) throws JMSException {
				TextMessage message = session.createTextMessage(payload);
				return message;
			}
		});
	}

}
