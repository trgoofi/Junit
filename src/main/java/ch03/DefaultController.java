package ch03;

import java.util.HashMap;
import java.util.Map;

public class DefaultController implements IController {
	private Map<String, IRequestHandler> requestHandlers
				= new HashMap<String, IRequestHandler>();

	public IRequestHandler getHandler(IRequest request) {
		if (!this.requestHandlers.containsKey(request.getName())) {
			String message = String.format(
									"Cannot find handler for request name [%s]",
									request.getName());
			throw new RuntimeException(message);
		}
		return this.requestHandlers.get(request.getName());
	}

	@Override
	public void addHandler(IRequest request, IRequestHandler requestHandler) {
		if (this.requestHandlers.containsKey(request.getName())) {
			throw new RuntimeException(
					String.format(
							"A request handler has already been " +
							"registered for request name [%s]",
							request.getName()));
		}else {
			this.requestHandlers.put(request.getName(), requestHandler);
		}
	}

	@Override
	public IResponse processRequest(IRequest request) {
		IResponse response;
		try{
			response = getHandler(request).process(request);
		}catch(Exception ex){
			response = new ErrorResponse(request, ex);
		}
		return response;
	}
}
