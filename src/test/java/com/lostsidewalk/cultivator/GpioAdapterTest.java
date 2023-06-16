package com.lostsidewalk.cultivator;

import com.pi4j.io.gpio.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static com.pi4j.io.gpio.PinPullResistance.PULL_DOWN;
import static com.pi4j.io.gpio.PinState.HIGH;
import static com.pi4j.io.gpio.RaspiPin.GPIO_02;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GpioAdapterTest {

    @Mock
    private GpioController gpioController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testProvisionDigitalOutputPinWithController() {
        Pin pin = RaspiPin.GPIO_01;
        PinState state = HIGH;
        GpioPinDigitalOutput expectedPin = mock(GpioPinDigitalOutput.class);
        when(gpioController.provisionDigitalOutputPin(pin, state)).thenReturn(expectedPin);

        GpioAdapter gpioAdapter = new GpioAdapter(gpioController);
        GpioPinDigitalOutput actualPin = gpioAdapter.provisionDigitalOutputPin(pin, state);

        assertEquals(expectedPin, actualPin);
        verify(gpioController).provisionDigitalOutputPin(pin, state);
    }

    @Test
    void testProvisionDigitalOutputPinWithoutController() {
        Pin pin = RaspiPin.GPIO_01;

        GpioAdapter gpioAdapter = new GpioAdapter();
        GpioPinDigitalOutput actualPin = gpioAdapter.provisionDigitalOutputPin(pin, HIGH);

        assertNull(actualPin);
        verify(gpioController, never()).provisionDigitalOutputPin(any(Pin.class), any(PinState.class));
    }

    @Test
    void testProvisionDigitalInputPinWithController() {
        Pin pin = GPIO_02;
        PinPullResistance pullDown = PULL_DOWN;
        GpioPinDigitalInput expectedPin = mock(GpioPinDigitalInput.class);
        when(gpioController.provisionDigitalInputPin(pin, pullDown)).thenReturn(expectedPin);

        GpioAdapter gpioAdapter = new GpioAdapter(gpioController);
        GpioPinDigitalInput actualPin = gpioAdapter.provisionDigitalInputPin(pin, pullDown);

        assertEquals(expectedPin, actualPin);
        verify(gpioController).provisionDigitalInputPin(pin, pullDown);
    }

    @Test
    void testProvisionDigitalInputPinWithoutController() {
        GpioAdapter gpioAdapter = new GpioAdapter();
        GpioPinDigitalInput actualPin = gpioAdapter.provisionDigitalInputPin(GPIO_02, PULL_DOWN);

        assertNull(actualPin);
        verify(gpioController, never()).provisionDigitalInputPin(any(Pin.class), any(PinPullResistance.class));
    }
}
