package sk.project22.pmacko;

public class NoFileLoadedException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public NoFileLoadedException()
	{
		super();
	}
	
	public NoFileLoadedException(Exception ex){
		super(ex);
	}

}
