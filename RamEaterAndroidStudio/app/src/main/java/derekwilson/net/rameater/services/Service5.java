package derekwilson.net.rameater.services;

import derekwilson.net.rameater.R;
import derekwilson.net.rameater.services.EaterService;

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

