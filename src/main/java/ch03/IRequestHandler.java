package ch03;

public interface IRequestHandler {
	IResponse process(IRequest request) throws Exception;
}
