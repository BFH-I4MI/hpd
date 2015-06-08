package ch.vivates.tools.sec;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.opensaml.common.SAMLException;
import org.opensaml.xml.security.credential.Credential;
import org.opensaml.xml.security.x509.KeyStoreX509CredentialAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;


public class CertificateStore {

	private static final Logger LOG = LoggerFactory.getLogger(CertificateStore.class);

	protected static final String ALIAS_DELIMITER = ",";

	protected static final String JAVA_KEYSTORE = "jks";

	private String[] aliases;

	private ConcurrentMap<String, Credential> credentials;

	private String keystorePath, keystoreType;

	private char[] keystorePasswd;

	/**
	 * @param keystorePath
	 * @param keystorePasswd
	 * @param keystoreAliases
	 * @throws SAMLException
	 */
	protected CertificateStore(String keystorePath, String keystorePasswd, String keystoreAliases) throws SAMLException {
		this(keystorePath, keystorePasswd, null, keystoreAliases);
	}

	/**
	 * @param keystorePath
	 * @param keystorePasswd
	 * @param keystoreType
	 * @param keystoreAliases
	 * @throws SAMLException
	 */
	protected CertificateStore(String keystorePath, String keystorePasswd, String keystoreType, String keystoreAliases)
			throws SAMLException {
		this.keystorePath = keystorePath;
		this.keystoreType = keystoreType;

		if (StringUtils.isEmpty(this.keystoreType)) {
			this.keystoreType = JAVA_KEYSTORE;
		}

		if (StringUtils.hasText(keystorePasswd)) {
			this.keystorePasswd = keystorePasswd.toCharArray();
		}

		if (StringUtils.hasText(keystoreAliases)) {
			String[] tempStrArray = keystoreAliases.split(ALIAS_DELIMITER);
			List<String> requestedAliases = new ArrayList<String>(tempStrArray.length);

			for (int index = 0; index < tempStrArray.length; index++) {
				String requestedAlias = tempStrArray[index].trim();

				if (StringUtils.hasText(requestedAlias)) {
					requestedAliases.add(requestedAlias);
				}
			}

			if (requestedAliases.size() > 0) {
				aliases = requestedAliases.toArray(new String[requestedAliases.size()]);
			}
		}

		initialize();
	}

	/**
	 * @throws SAMLException
	 */
	private void initialize() throws SAMLException {
		FileInputStream fis = null;
		this.credentials = new ConcurrentHashMap<String, Credential>();

		try {
			KeyStore keystore = KeyStore.getInstance(keystoreType);

			fis = new FileInputStream(keystorePath);
			keystore.load(fis, this.keystorePasswd);

			if (aliases == null) {
				List<String> availableAliases = new ArrayList<String>();

				for (Enumeration<String> keystoreAliases = keystore.aliases(); keystoreAliases.hasMoreElements();) {
					availableAliases.add(keystoreAliases.nextElement());
				}

				aliases = availableAliases.toArray(new String[availableAliases.size()]);
			}

			if (aliases.length == 0) {
				throw new SAMLException("No alias has been provided for the keystore"
						+ "and the keystore itself contains no entries at all.");
			}

			for (String alias : aliases) {
				Certificate certificate = keystore.getCertificate(alias);

				if (certificate != null) {
					credentials.put(alias, new KeyStoreX509CredentialAdapter(keystore, alias, keystorePasswd));
				}
			}

			if (credentials.isEmpty()) {
				throw new SAMLException("The keystore contains no certificates identified by the provided aliases.");
			}

			LOG.info("CertificateStore has been successfully initialized using the following keystore:");
			LOG.info("- path: " + keystorePath);
			LOG.info("- type: " + keystoreType);
			LOG.info("- requested aliases: " + StringUtils.arrayToCommaDelimitedString(aliases));
		} catch (FileNotFoundException fnfExcp) {
			throw new SAMLException("Keystore can not be located at: " + this.keystorePath, fnfExcp);
		} catch (IOException ioExcp) {
			throw new SAMLException("I/O failure occured upon processing the keystore.", ioExcp);
		} catch (GeneralSecurityException ksExcp) {
			throw new SAMLException("Keystore can not be processed.", ksExcp);
		} catch (RuntimeException rtExcp) {
			throw new SAMLException("Keystore can not be processed.", rtExcp);
		} finally {
			try {
				if (fis != null) {
					fis.close();
				}
			} catch (IOException ioExcp) {
				throw new SAMLException("I/O failure occured upon closing the keystore.", ioExcp);
			}
		}
	}

	/**
	 * @param alias
	 * @return
	 */
	protected Credential getCredential(String alias) {
		return credentials.get(alias);
	}

	/**
	 * @return
	 */
	protected Map<String, Credential> getCredentials() {
		return credentials;
	}
}