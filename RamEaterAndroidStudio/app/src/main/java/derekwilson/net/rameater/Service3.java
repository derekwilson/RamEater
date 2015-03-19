package derekwilson.net.rameater;

public class Service3 extends EaterService {
    @Override
    protected int getServiceId() {
        return R.string.service_3;
    }

    @Override
    protected String getServiceName() {
        return getString(R.string.service_3);
    }
}

