package ch03;

public interface IController {
	IResponse processRequest(IRequest request);

	void addHandler(IRequest request, IRequestHandler requestHandler);
}
