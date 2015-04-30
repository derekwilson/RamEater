package derekwilson.net.rameater.services;

import derekwilson.net.rameater.R;

public class Service6 extends EaterService {
    @Override
    protected int getServiceId() {
        return R.string.service_6;
    }

    @Override
    protected String getServiceName() {
        return getString(R.string.service_6);
    }
}
