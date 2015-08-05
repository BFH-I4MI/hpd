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

/**
 * The Class ExceptionProcessor processes exception thrown from the directory.
 * 
 * @author Kevin Tippenhauer, Berner Fachhochschule
 */
public class ExceptionProcessor implements Processor {

	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory
			.getLogger("PrepareProcessor");

	/** The jms template. */
	protected JmsTemplate jmsTemplate;
	
	/** The payload. */
	private String payload;

	/**
	 * Gets the jms template.
	 *
	 * @return the jms template
	 */
	public JmsTemplate getJmsTemplate() {
		return jmsTemplate;
	}

	/**
	 * Sets the jms template.
	 *
	 * @param jmsTemplate the new jms template
	 */
	public void setJmsTemplate(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}

	/* (non-Javadoc)
	 * @see org.apache.camel.Processor#process(org.apache.camel.Exchange)
	 */
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
