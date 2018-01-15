package servicenow.soap;

import java.net.URI;

public class XmlParseException extends SoapResponseException {

	private static final long serialVersionUID = 1L;

	public XmlParseException(URI uri, Exception cause) {
		super(uri, cause);
	}


}