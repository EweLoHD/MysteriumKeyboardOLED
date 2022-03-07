package lu.ewelo.qmk.oled;

import org.apache.hc.core5.http.ParseException;
import org.hid4java.*;
import org.hid4java.event.HidServicesEvent;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.SpotifyHttpManager;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.User;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;
import se.michaelthelin.spotify.requests.data.users_profile.GetCurrentUsersProfileRequest;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;

public class Test implements HidServicesListener {

    public Test() {
        // Configure to use custom specification
        HidServicesSpecification hidServicesSpecification = new HidServicesSpecification();

        // Use the v0.7.0 manual start feature to get immediate attach events
        hidServicesSpecification.setAutoStart(false);

        // Get HID services using custom specification
        HidServices hidServices = HidManager.getHidServices(hidServicesSpecification);
        hidServices.addHidServicesListener(this);

        // Manually start the services to get attachment event
        hidServices.start();

        HidDevice keyboard = null;

        // Provide a list of attached devices
        for (HidDevice hidDevice : hidServices.getAttachedHidDevices()) {
            if (hidDevice.getProduct() != null
                    && hidDevice.getProduct().equalsIgnoreCase("MYSTERIUM")
                    && hidDevice.getUsage() == 0x61) {
                System.out.println(hidDevice);
                keyboard = hidDevice;
            }
        }

        keyboard.open();

        byte[] data = "qwertzuiopasdfghjklyxx".getBytes(StandardCharsets.UTF_8);
        //byte[] data = "Hello\nTest\nOMG wow\nwait fdhwf".getBytes(StandardCharsets.UTF_8);
        //byte[] data = new byte[]{(byte) 0xdf, (byte) 0xde}; //- .
        keyboard.write(data, data.length, (byte) 0x00);
    }

    public static void main(String[] args) {
        new Test();

        /*SpotifyApi spotifyApi = new SpotifyApi.Builder()
                .setClientId("")
                .setClientSecret("")
                .setRedirectUri(SpotifyHttpManager.makeUri("https://example.com/spotify-redirect"))
                .build();


        AuthorizationCodeUriRequest authorizationCodeUriRequest = spotifyApi.authorizationCodeUri()
//          .state("x4xkmn9pu3j6ukrs8n")
//          .scope("user-read-birthdate,user-read-email")
             .show_dialog(true)
            .build();

        URI uri = authorizationCodeUriRequest.execute();

        System.out.println("URI: " + uri.toString());*/
    }

    @Override
    public void hidDeviceAttached(HidServicesEvent event) {
        //System.out.println(event);
    }

    @Override
    public void hidDeviceDetached(HidServicesEvent event) {
        //System.out.println(event);
    }

    @Override
    public void hidFailure(HidServicesEvent event) {
        //System.out.println(event);
    }
}
