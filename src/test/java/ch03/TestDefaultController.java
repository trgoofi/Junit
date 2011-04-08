package ch03;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class TestDefaultController {
	private DefaultController controller;
	private IRequest request;
	private IRequestHandler handler;

	private class SampleRequest implements IRequest {
		public String getName() {
			return "Test";
		}
	}

	private class SampleHandler implements IRequestHandler {
		public IResponse process(IRequest request) throws Exception {
			return new SampleResponse();
		}
	}

	private class SampleResponse implements IResponse {
	}

	@Before
	public void instantiate() throws Exception {
		this.controller = new DefaultController();
		this.request = new SampleRequest();
		this.handler = new SampleHandler();
		
		this.controller.addHandler(request, handler);
	}

	@Test
	public void testAddHandler() {
		IRequestHandler handler2 = this.controller.getHandler(request);
		assertSame(handler2, handler);
	}
	
	@Test
	public void testProcessRequest(){
		IResponse response = this.controller.processRequest(request);
		assertNotNull("Must not return a null response", response);
		assertEquals("Response should be of type SampleResponse",
					 SampleResponse.class, response.getClass());
	}
}
