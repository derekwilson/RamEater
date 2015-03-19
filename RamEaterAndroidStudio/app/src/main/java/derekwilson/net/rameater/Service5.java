package derekwilson.net.rameater;

public class Service5 extends EaterService {
    @Override
    protected int getServiceId() {
        return R.string.service_5;
    }

    @Override
    protected String getServiceName() {
        return getString(R.string.service_5);
    }
}

