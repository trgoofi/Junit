package ch03;

public class ErrorResponse implements IResponse {
	private IRequest originalRequest;
	private Exception originalException;

	public ErrorResponse(IRequest request, Exception ex) {
		this.originalRequest = request;
		this.originalException = ex;
	}

	public IRequest getOriginalRequest(){
		return this.originalRequest;
	}
	
	public Exception getOriginalException(){
		return this.originalException;
	}
}
