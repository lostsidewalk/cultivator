package com.lostsidewalk.cultivator;

import com.pi4j.io.gpio.*;

public class GpioAdapter {

    private final GpioController gpioController;

    public GpioAdapter(GpioController gpioController) {
        this.gpioController = gpioController;
    }

    public GpioAdapter() {
        this.gpioController = null;
    }

    public GpioPinDigitalOutput provisionDigitalOutputPin(Pin pinByAddress, PinState state) {
        if (gpioController != null) {
            return gpioController.provisionDigitalOutputPin(pinByAddress, state);
        }
        return null;
    }

    public GpioPinDigitalInput provisionDigitalInputPin(Pin pinByAddress, PinPullResistance pullDown) {
        if (gpioController != null) {
            return gpioController.provisionDigitalInputPin(pinByAddress, pullDown);
        }
        return null;
    }
}
