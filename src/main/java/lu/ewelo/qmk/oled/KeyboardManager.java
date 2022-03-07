package lu.ewelo.qmk.oled;

import org.hid4java.HidManager;
import org.hid4java.HidServices;
import org.hid4java.HidServicesListener;
import org.hid4java.HidServicesSpecification;
import org.hid4java.event.HidServicesEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class KeyboardManager implements HidServicesListener {

    private static final Logger logger = LoggerFactory.getLogger(KeyboardManager.class);

    public static final int QMK_DEFAULT_USAGE_ID = 0x61;

    private final HidServices hidServices;

    public KeyboardManager() {
        // Configure to use custom specification
        HidServicesSpecification hidServicesSpecification = new HidServicesSpecification();

        // Use the v0.7.0 manual start feature to get immediate attach events
        hidServicesSpecification.setAutoStart(false);

        // Get HID services using custom specification
        this.hidServices = HidManager.getHidServices(hidServicesSpecification);
        hidServices.addHidServicesListener(this);

        // Manually start the services to get attachment event
        hidServices.start();
    }

    public List<Keyboard> getAttachedKeyboards(int usage) {
        return hidServices.getAttachedHidDevices()
                .stream()
                .filter(hidDevice -> hidDevice.getUsage() == usage)
                .map(Keyboard::new)
                .collect(Collectors.toList());
    }

    @Override
    public void hidDeviceAttached(HidServicesEvent event) {

    }

    @Override
    public void hidDeviceDetached(HidServicesEvent event) {

    }

    @Override
    public void hidFailure(HidServicesEvent event) {

    }

    public HidServices getHidServices() {
        return hidServices;
    }
}
