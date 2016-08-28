package taxcalculator.exception;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AccumulatedTranslationException extends Exception {

	private List<DomainObjectTranslationException> exceptions = new ArrayList<DomainObjectTranslationException>();
	
	public AccumulatedTranslationException() {
		super();
	}
	
	public void addException(DomainObjectTranslationException exception) {
		exceptions.add(exception);
	}
	
	public boolean hasErrors() {
		return !exceptions.isEmpty();
	}
	
	public List<String> getErrorMessages() {
		return exceptions.stream()
				.map(e -> e.getMessage())
				.collect(Collectors.toList());
	}
	
	@Override
	public String getMessage() {
		return String.join("; ", getErrorMessages());
	}


}
