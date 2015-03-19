package derekwilson.net.rameater;

public class Service2 extends EaterService {
	@Override
	protected int getServiceId() {
		return R.string.service_2;
	}

	@Override
	protected String getServiceName() {
		return getString(R.string.service_2);
	}
}

