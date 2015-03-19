package derekwilson.net.rameater;

public class Service4 extends EaterService {
    @Override
    protected int getServiceId() {
        return R.string.service_4;
    }

    @Override
    protected String getServiceName() {
        return getString(R.string.service_4);
    }
}

