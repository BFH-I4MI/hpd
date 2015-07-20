package ch.vivates.tools.dsmlv2;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import org.apache.directory.api.dsmlv2.DsmlDecorator;
import org.apache.directory.api.dsmlv2.Dsmlv2Parser;
import org.apache.directory.api.dsmlv2.ParserUtils;
import org.apache.directory.api.dsmlv2.request.BatchRequestDsml;
import org.apache.directory.api.dsmlv2.request.BatchRequestDsml.OnError;
import org.apache.directory.api.dsmlv2.request.BatchRequestDsml.Processing;
import org.apache.directory.api.dsmlv2.request.BatchRequestDsml.ResponseOrder;
import org.apache.directory.api.dsmlv2.request.Dsmlv2Grammar;
import org.apache.directory.api.dsmlv2.response.AddResponseDsml;
import org.apache.directory.api.dsmlv2.response.BatchResponseDsml;
import org.apache.directory.api.dsmlv2.response.BindResponseDsml;
import org.apache.directory.api.dsmlv2.response.CompareResponseDsml;
import org.apache.directory.api.dsmlv2.response.DelResponseDsml;
import org.apache.directory.api.dsmlv2.response.ErrorResponse;
import org.apache.directory.api.dsmlv2.response.ErrorResponse.ErrorResponseType;
import org.apache.directory.api.dsmlv2.response.ExtendedResponseDsml;
import org.apache.directory.api.dsmlv2.response.ModDNResponseDsml;
import org.apache.directory.api.dsmlv2.response.ModifyResponseDsml;
import org.apache.directory.api.dsmlv2.response.SearchResponseDsml;
import org.apache.directory.api.dsmlv2.response.SearchResultDoneDsml;
import org.apache.directory.api.dsmlv2.response.SearchResultEntryDsml;
import org.apache.directory.api.dsmlv2.response.SearchResultReferenceDsml;
import org.apache.directory.api.i18n.I18n;
import org.apache.directory.api.ldap.model.cursor.SearchCursor;
import org.apache.directory.api.ldap.model.message.AbandonRequest;
import org.apache.directory.api.ldap.model.message.AddRequest;
import org.apache.directory.api.ldap.model.message.AddResponse;
import org.apache.directory.api.ldap.model.message.BindRequest;
import org.apache.directory.api.ldap.model.message.BindResponse;
import org.apache.directory.api.ldap.model.message.CompareRequest;
import org.apache.directory.api.ldap.model.message.CompareResponse;
import org.apache.directory.api.ldap.model.message.DeleteRequest;
import org.apache.directory.api.ldap.model.message.DeleteResponse;
import org.apache.directory.api.ldap.model.message.ExtendedRequest;
import org.apache.directory.api.ldap.model.message.ExtendedResponse;
import org.apache.directory.api.ldap.model.message.MessageTypeEnum;
import org.apache.directory.api.ldap.model.message.ModifyDnRequest;
import org.apache.directory.api.ldap.model.message.ModifyDnResponse;
import org.apache.directory.api.ldap.model.message.ModifyRequest;
import org.apache.directory.api.ldap.model.message.ModifyResponse;
import org.apache.directory.api.ldap.model.message.Request;
import org.apache.directory.api.ldap.model.message.Response;
import org.apache.directory.api.ldap.model.message.ResultCodeEnum;
import org.apache.directory.api.ldap.model.message.SearchRequest;
import org.apache.directory.api.ldap.model.message.SearchResultDone;
import org.apache.directory.api.ldap.model.message.SearchResultEntry;
import org.apache.directory.api.ldap.model.message.SearchResultReference;
import org.apache.directory.ldap.client.api.LdapConnection;
import org.apache.directory.ldap.client.api.LdapConnectionPool;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmlpull.v1.XmlPullParserException;

/**
 * The Class Dsmlv2Engine processes the DSML Message.
 * 
 * @author Federico Marmory, Post CH, major development
 * @author Kevin Tippenhauer, Berner Fachhochschule, javadoc
 */
public class Dsmlv2Engine {

	/** The grammar. */
	private final Dsmlv2Grammar grammar = new Dsmlv2Grammar();

	/** The LDAP connection pool. */
	private LdapConnectionPool ldapConnectionPool;

	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(Dsmlv2Engine.class);

	/**
	 * Sets the LDAP connection pool.
	 *
	 * @param ldapConnectionPool the new LDAP connection pool
	 */
	public void setLdapConnectionPool(LdapConnectionPool ldapConnectionPool) {
		this.ldapConnectionPool = ldapConnectionPool;
	}

	/**
	 * Processes the DSML based on InputStream and OutputStream.
	 *
	 * @param inputStream the InputStream
	 * @param out the OutputStream
	 * @throws Exception the exception
	 */
	public void processDSML(InputStream inputStream, OutputStream out) throws Exception {
		processDSML(inputStream, "UTF-8", out);
	}

	/**
	 * Processes the DSML based on InputStream, input encoding and OutputStream.
	 *
	 * @param inputStream the InputStream
	 * @param inputEncoding the input encoding as String
	 * @param out the OutputStream
	 * @throws Exception the exception
	 */
	public void processDSML(InputStream inputStream, String inputEncoding, OutputStream out) throws Exception {
		Dsmlv2Parser parser = new Dsmlv2Parser(grammar);
		parser.setInput(inputStream, inputEncoding);
		processDSML(out, parser);
	}

	/**
	 * Processes the DSML based on OutputStream and Dsmlv2Parser.
	 *
	 * @param outStream the OutputStream
	 * @param parser the Dsmlv2Parser
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	protected void processDSML(OutputStream outStream, Dsmlv2Parser parser) throws IOException {
		BufferedWriter respWriter = null;

		BatchRequestDsml batchRequest = null;
		BatchResponseDsml batchResponse = new BatchResponseDsml();

		boolean continueOnError = false;

		if (outStream != null) {
			respWriter = new BufferedWriter(new OutputStreamWriter(outStream));
		}

		// Processing BatchRequest:
		// - Parsing and Getting BatchRequest
		// - Getting and registering options from BatchRequest
		try {
			parser.parseBatchRequest();

			batchRequest = parser.getBatchRequest();

			if (OnError.RESUME.equals(batchRequest.getOnError())) {
				continueOnError = true;
			} else if (OnError.EXIT.equals(batchRequest.getOnError())) {
				continueOnError = false;
			}

			if ((batchRequest.getRequestID() != 0) && (batchResponse != null)) {
				batchResponse.setRequestID(batchRequest.getRequestID());
			}

		} catch (XmlPullParserException e) {
			// We create a new ErrorResponse and return the XML response.
			ErrorResponse errorResponse = new ErrorResponse(0, ErrorResponseType.MALFORMED_REQUEST, I18n.err(I18n.ERR_03001,
					e.getLocalizedMessage(), e.getLineNumber(), e.getColumnNumber()));

			batchResponse.addResponse(errorResponse);

			if (respWriter != null) {
				respWriter.write(batchResponse.toDsml());
				respWriter.flush();
			}

			return;
		}

		if (respWriter != null) {
			StringBuilder sb = new StringBuilder();

			sb.append("<batchResponse ");

			sb.append(ParserUtils.DSML_NAMESPACE.asXML());

			sb.append(" "); // a space to separate the namespace declarations

			sb.append(ParserUtils.XSD_NAMESPACE.asXML());

			sb.append(" "); // a space to separate the namespace declarations

			sb.append(ParserUtils.XSI_NAMESPACE.asXML());

			sb.append(" requestID=\"");
			sb.append(batchRequest.getRequestID());
			sb.append("\">");

			respWriter.write(sb.toString());
		}

		// Processing each request:
		// - Getting a new request
		// - Checking if the request is well formed
		// - Sending the request to the server
		// - Getting and converting reponse(s) as XML
		// - Looping until last request
		DsmlDecorator<? extends Request> request = null;

		try {
			request = parser.getNextRequest();
		} catch (XmlPullParserException e) {
			LOG.warn("Failed while getting next request", e);

			int reqId = 0;

			// We create a new ErrorResponse and return the XML response.
			ErrorResponse errorResponse = new ErrorResponse(reqId, ErrorResponseType.MALFORMED_REQUEST, I18n.err(I18n.ERR_03001,
					e.getLocalizedMessage(), e.getLineNumber(), e.getColumnNumber()));

			batchResponse.addResponse(errorResponse);

			if (respWriter != null) {
				respWriter.write(batchResponse.toDsml());
				respWriter.flush();
			}

			return;
		}

		while (request != null) // (Request == null when there's no more request
								// to process)
		{
			// Checking the request has a requestID attribute if Processing =
			// Parallel and ResponseOrder = Unordered
			if ((batchRequest.getProcessing().equals(Processing.PARALLEL))
					&& (batchRequest.getResponseOrder().equals(ResponseOrder.UNORDERED)) && (request.getDecorated().getMessageId() <= 0)) {
				// Then we have to send an errorResponse
				ErrorResponse errorResponse = new ErrorResponse(0, ErrorResponseType.MALFORMED_REQUEST, I18n.err(I18n.ERR_03002));

				if (respWriter != null) {
					writeResponse(respWriter, errorResponse);
				} else {
					batchResponse.addResponse(errorResponse);
				}

				break;
			}

			try {
				ResultCodeEnum resultCode = processRequest(request, respWriter, batchResponse);
				// Checking if we need to exit processing (if an error has
				// occurred if onError == Exit)
				if ((!continueOnError) && (resultCode != null) && (resultCode != ResultCodeEnum.SUCCESS)
						&& (resultCode != ResultCodeEnum.COMPARE_TRUE) && (resultCode != ResultCodeEnum.COMPARE_FALSE)
						&& (resultCode != ResultCodeEnum.REFERRAL)) {
					break;
				}
			} catch (Exception e) {
				LOG.warn("Failed to process request", e);

				// We create a new ErrorResponse and return the XML response.
				ErrorResponse errorResponse = new ErrorResponse(request.getDecorated().getMessageId(),
						ErrorResponseType.GATEWAY_INTERNAL_ERROR, I18n.err(I18n.ERR_03003, e.getMessage()));

				if (respWriter != null) {
					writeResponse(respWriter, errorResponse);
				} else {
					batchResponse.addResponse(errorResponse);
				}

				break;
			}

			// Getting next request
			try {
				request = parser.getNextRequest();
			} catch (XmlPullParserException e) {
				// We create a new ErrorResponse and return the XML response.
				ErrorResponse errorResponse = new ErrorResponse(0, ErrorResponseType.MALFORMED_REQUEST, I18n.err(I18n.ERR_03001,
						e.getLocalizedMessage(), e.getLineNumber(), e.getColumnNumber()));

				if (respWriter != null) {
					writeResponse(respWriter, errorResponse);
				} else {
					batchResponse.addResponse(errorResponse);
				}

				break;
			}
		}

		if (respWriter != null) {
			respWriter.write("</batchResponse>");
			respWriter.flush();
		}
	}

	/**
	 * Writes the response.
	 *
	 * @param respWriter the response BufferedWriter
	 * @param respDsml the response DSML
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	protected void writeResponse(BufferedWriter respWriter, DsmlDecorator<?> respDsml) throws IOException {
		if (respWriter != null) {
			Element xml = respDsml.toDsml(null);
			xml.write(respWriter);
		}
	}

	/**
	 * Processes DSML request.
	 *
	 * @param request the request
	 * @param respWriter the response writer
	 * @param batchResponse the batch response
	 * @return the ResultCodeEnum
	 * @throws Exception the exception
	 */
	protected ResultCodeEnum processRequest(DsmlDecorator<? extends Request> request, BufferedWriter respWriter,
			BatchResponseDsml batchResponse) throws Exception {
		ResultCodeEnum resultCode = null;
		LdapConnection connection = ldapConnectionPool.getConnection();
		try {
			switch (request.getDecorated().getType()) {
			case ABANDON_REQUEST:
				connection.abandon((AbandonRequest) request);
				return null;

			case ADD_REQUEST:
				AddResponse response = connection.add((AddRequest) request);
				resultCode = response.getLdapResult().getResultCode();
				AddResponseDsml addResponseDsml = new AddResponseDsml(connection.getCodecService(), response);
				writeResponse(respWriter, addResponseDsml);

				break;

			case BIND_REQUEST:
				BindResponse bindResponse = connection.bind((BindRequest) request);
				resultCode = bindResponse.getLdapResult().getResultCode();
				BindResponseDsml authResponseDsml = new BindResponseDsml(connection.getCodecService(), bindResponse);
				writeResponse(respWriter, authResponseDsml);

				break;

			case COMPARE_REQUEST:
				CompareResponse compareResponse = connection.compare((CompareRequest) request);
				resultCode = compareResponse.getLdapResult().getResultCode();
				CompareResponseDsml compareResponseDsml = new CompareResponseDsml(connection.getCodecService(), compareResponse);
				writeResponse(respWriter, compareResponseDsml);

				break;

			case DEL_REQUEST:
				DeleteResponse delResponse = connection.delete((DeleteRequest) request);
				resultCode = delResponse.getLdapResult().getResultCode();
				DelResponseDsml delResponseDsml = new DelResponseDsml(connection.getCodecService(), delResponse);
				writeResponse(respWriter, delResponseDsml);

				break;

			case EXTENDED_REQUEST:
				ExtendedResponse extendedResponse = connection.extended((ExtendedRequest) request);
				resultCode = extendedResponse.getLdapResult().getResultCode();
				ExtendedResponseDsml extendedResponseDsml = new ExtendedResponseDsml(connection.getCodecService(), extendedResponse);
				writeResponse(respWriter, extendedResponseDsml);

				break;

			case MODIFY_REQUEST:
				ModifyResponse modifyResponse = connection.modify((ModifyRequest) request);
				resultCode = modifyResponse.getLdapResult().getResultCode();
				ModifyResponseDsml modifyResponseDsml = new ModifyResponseDsml(connection.getCodecService(), modifyResponse);
				writeResponse(respWriter, modifyResponseDsml);

				break;

			case MODIFYDN_REQUEST:
				ModifyDnResponse modifyDnResponse = connection.modifyDn((ModifyDnRequest) request);
				resultCode = modifyDnResponse.getLdapResult().getResultCode();
				ModDNResponseDsml modDNResponseDsml = new ModDNResponseDsml(connection.getCodecService(), modifyDnResponse);
				writeResponse(respWriter, modDNResponseDsml);

				break;

			case SEARCH_REQUEST:
				SearchCursor searchResponses = connection.search((SearchRequest) request);

				SearchResponseDsml searchResponseDsml = new SearchResponseDsml(connection.getCodecService());

				if (respWriter != null) {
					StringBuilder sb = new StringBuilder();
					sb.append("<searchResponse");

					if (request.getDecorated().getMessageId() > 0) {
						sb.append(" requestID=\"");
						sb.append(request.getDecorated().getMessageId());
						sb.append('"');
					}

					sb.append('>');

					respWriter.write(sb.toString());
				}

				while (searchResponses.next()) {
					Response searchResponse = searchResponses.get();

					if (searchResponse.getType() == MessageTypeEnum.SEARCH_RESULT_ENTRY) {
						SearchResultEntry searchResultEntry = (SearchResultEntry) searchResponse;

						SearchResultEntryDsml searchResultEntryDsml = new SearchResultEntryDsml(connection.getCodecService(),
								searchResultEntry);
						searchResponseDsml = new SearchResponseDsml(connection.getCodecService(), searchResultEntryDsml);

						if (respWriter != null) {
							writeResponse(respWriter, searchResultEntryDsml);
						} else {
							searchResponseDsml.addResponse(searchResultEntryDsml);
						}
					} else if (searchResponse.getType() == MessageTypeEnum.SEARCH_RESULT_REFERENCE) {
						SearchResultReference searchResultReference = (SearchResultReference) searchResponse;

						SearchResultReferenceDsml searchResultReferenceDsml = new SearchResultReferenceDsml(connection.getCodecService(),
								searchResultReference);
						searchResponseDsml = new SearchResponseDsml(connection.getCodecService(), searchResultReferenceDsml);

						if (respWriter != null) {
							writeResponse(respWriter, searchResultReferenceDsml);
						} else {
							searchResponseDsml.addResponse(searchResultReferenceDsml);
						}
					}
				}

				SearchResultDone srDone = searchResponses.getSearchResultDone();

				if (srDone != null) {
					resultCode = srDone.getLdapResult().getResultCode();

					SearchResultDoneDsml srdDsml = new SearchResultDoneDsml(connection.getCodecService(), srDone);

					if (respWriter != null) {
						writeResponse(respWriter, srdDsml);
						respWriter.write("</searchResponse>");
					} else {
						searchResponseDsml.addResponse(srdDsml);
						batchResponse.addResponse(searchResponseDsml);
					}
				}

				break;

			default:
				throw new IllegalStateException("Unexpected request tpye " + request.getDecorated().getType());
			}
		} finally {
			ldapConnectionPool.releaseConnection(connection);
		}

		return resultCode;
	}

}
