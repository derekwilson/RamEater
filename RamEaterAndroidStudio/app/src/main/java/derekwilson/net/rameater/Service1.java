package derekwilson.net.rameater;

public class Service1 extends EaterService {
	@Override
	protected int getServiceId() {
		return R.string.service_1;
	}

	@Override
	protected String getServiceName() {
		return getString(R.string.service_1);
	}
}
