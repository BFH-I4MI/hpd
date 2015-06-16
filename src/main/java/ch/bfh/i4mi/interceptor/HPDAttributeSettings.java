package ch.bfh.i4mi.interceptor;

public class HPDAttributeSettings {
	
	private String attributeName;
	private Boolean isSingleValue;
	private Boolean isOptional;
	private int numberOfMinOccurence;
	private int numberOfMaxOccurence;
	
	
	public void setAttributeName(String anAttributeName) throws IllegalArgumentException {
		if(anAttributeName == null || anAttributeName.isEmpty()) {
			throw new IllegalArgumentException();
		}
		this.attributeName = anAttributeName;
	}

	public void setIsSingleValue(Boolean anIsSingleValue) throws IllegalArgumentException {
		if(anIsSingleValue == null) {
			throw new IllegalArgumentException();
		}
		this.isSingleValue = anIsSingleValue;
	}

	public void setIsOptional(Boolean anIsOptional) throws IllegalArgumentException {
		if(anIsOptional == null) {
			throw new IllegalArgumentException();
		}
		this.isOptional = anIsOptional;
	}

	public void setMinMaxOccurence(int min, int max) throws IllegalArgumentException {
		
		if(min < 0) {
			throw new IllegalArgumentException("'min' is lesser than 0!");
		} else if (max < 1) {
			throw new IllegalArgumentException("'max' is lesser than 1!");
		} else if (min > max) {
			throw new IllegalArgumentException("'max' is lesser than 'min'!");
		}
		this.numberOfMinOccurence = min;
		this.numberOfMaxOccurence = max;
	}
	
	public String getAttributeName() {
		return attributeName;
	}
	
	public Boolean isSingleValue() {
		return isSingleValue;
	}
	public Boolean isOptional() {
		return isOptional;
	}
	public int getNumberOfMinOccurence() {
		return numberOfMinOccurence;
	}
	
	public int getNumberOfMaxOccurence() {
		return numberOfMaxOccurence;
	}
	
	public HPDAttributeSettings(String anAttributeName,
						Boolean anIsSingleValue,
						Boolean anIsOptional,
						int aNumberOfMinOccurence,
						int aNumberOfMaxOccurence) throws IllegalArgumentException {
		
		this.setAttributeName(anAttributeName);
		this.setIsSingleValue(anIsSingleValue);
		this.setIsOptional(anIsOptional);
		this.setMinMaxOccurence(aNumberOfMinOccurence, aNumberOfMaxOccurence);
	}
	
	
	
	

}
