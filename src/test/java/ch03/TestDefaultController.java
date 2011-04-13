package ch03;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class TestDefaultController {
	private DefaultController controller;
	private IRequest request;
	private IRequestHandler handler;

	private class SampleRequest implements IRequest {
		private static final String DEFAULT_NAME = "Test";
		private String name;
		
		public SampleRequest(String name){
			this.name = name;
		}
		
		public SampleRequest(){
			this(DEFAULT_NAME);
		}
		
		public String getName() {
			return this.name;
		}
	}

	private class SampleHandler implements IRequestHandler {
		public IResponse process(IRequest request) throws Exception {
			return new SampleResponse();
		}
	}

	public class SampleExceptionHandler implements IRequestHandler{

		@Override
		public IResponse process(IRequest request) throws Exception {
			throw new Exception("error processing request");
		}
		
	}
	
	private class SampleResponse implements IResponse {
		private static final String NAME = "Test";
		
		@Override
		public String getName() {
			return NAME;
		}
		
		@Override
		public boolean equals(Object object){
			boolean result = false;
			if(object instanceof SampleResponse){
				result = ((SampleResponse)object).getName().equals(getName());
			}
			return result;
		}
		
		@Override
		public int hashCode(){
			return NAME.hashCode();
		}
	}

	@Before
	public void instantiate() throws Exception {
		this.controller = new DefaultController();
		this.request = new SampleRequest();
		this.handler = new SampleHandler();
		
		this.controller.addHandler(request, handler);
	}

	@Test(timeout = 13)
	public void testProcessMultipleRequestsTimeout(){
		IRequest request;
		IResponse response = new SampleResponse();
		IRequestHandler handler = new SampleHandler();
		
		for(int i = 0; i < 99999; ++i){
			request = new SampleRequest(String.valueOf(i));
			this.controller.addHandler(request, handler);
			response = this.controller.processRequest(request);
			assertNotNull(response);
			assertNotSame(ErrorResponse.class, response.getClass());
		}
	}
	
	@Test(expected = RuntimeException.class)
	public void testGetHandlerNotDefined(){
		SampleRequest request = new SampleRequest("testNotDefined");
		
		// The following line is supposed to throw a RuntimeException
		this.controller.getHandler(request);
	}
	
	@Test(expected = RuntimeException.class)
	public void testAddRequestDuplicateName(){
		SampleRequest request = new SampleRequest();
		SampleHandler handler = new SampleHandler();
		
		// The following line is supposed to throw a RuntionException
		// instantiate() already add the default SampleHandler 
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
		assertEquals(new SampleResponse(), response);
	}
	
	@Test
	public void testProcessRequestAnswersErrorResponse() {
		SampleRequest request = new SampleRequest("testError");
		SampleExceptionHandler handler = new SampleExceptionHandler();
		this.controller.addHandler(request, handler);
		IResponse response = this.controller.processRequest(request);
		assertNotNull("Must not return a null response", response);
		assertEquals(ErrorResponse.class, response.getClass());
	}
}
