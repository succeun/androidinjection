package androidinjection.annotation;


public class ClassInvocation {
    protected Object receiver = null;
    
	public ClassInvocation(Object receiver) {
		this.receiver = receiver;
	}

	public Object getReceiver() {
		return receiver;
	}
	
	@Override
	public String toString() {
		return "[Receiver: " + receiver + "]";
	}
}
