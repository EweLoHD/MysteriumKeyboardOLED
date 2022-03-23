package lu.ewelo.qmk.oled.keyboard.screen.screens;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import lu.ewelo.qmk.oled.keyboard.screen.CustomScreen;
import lu.ewelo.qmk.oled.keyboard.screen.ScreenManager;
import lu.ewelo.qmk.oled.keyboard.screen.annotations.Screen;
import lu.ewelo.qmk.oled.util.Config;
import org.apache.hc.core5.http.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.SpotifyHttpManager;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import se.michaelthelin.spotify.model_objects.miscellaneous.CurrentlyPlayingContext;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRefreshRequest;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;
import se.michaelthelin.spotify.requests.data.player.GetInformationAboutUsersCurrentPlaybackRequest;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Screen(id="spotify", name="Spotify")
public class SpotifyScreen extends CustomScreen {

    private static final Logger logger = LoggerFactory.getLogger(SpotifyScreen.class);

    private SpotifyApi spotifyApi;
    private HttpServer httpServer;

    private Config credentialsConfig;

    private volatile String code;


    public SpotifyScreen(ScreenManager screenManager) {
        super(screenManager);

        this.credentialsConfig = new Config.ConfigBuilder()
                .setPath("spotify-credentials.properties")
                //.addDefault("access-token", "")
                .addDefault("refresh-token", "")
                .build();

        this.spotifyApi = new SpotifyApi.Builder()
                .setClientId(getConfig().get("spotify-client-id"))
                .setClientSecret(getConfig().get("spotify-client-secret"))
                .setRedirectUri(SpotifyHttpManager.makeUri("http://localhost:6969/spotify-redirect"))
                .build();

        try {
            refreshToken();
        } catch (IOException | ParseException | SpotifyWebApiException e) {
            e.printStackTrace();
        }
    }


    private void freshLogin() {
        // Creating a HttpServer for handling the redirect url
        try {
            this.httpServer = HttpServer.create(new InetSocketAddress(6969), 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        httpServer.createContext("/spotify-redirect", new RedirectHandler());
        httpServer.setExecutor(null); // creates a default executor
        httpServer.start();

        // Retrieve the URL where the user can log in
        AuthorizationCodeUriRequest authorizationCodeUriRequest = spotifyApi.authorizationCodeUri()
                .scope("user-read-playback-state,user-read-currently-playing")
                .show_dialog(true)
                .build();

        // The URL where the user can log in
        URI uri = authorizationCodeUriRequest.execute();
        logger.info("URI: " + uri.toString());

        // Tries to open the URL in the browser
        try {
            Desktop.getDesktop().browse(uri);
        } catch (IOException e) {
            logger.error("Cannot open Browser", e);
        }

        // The authorization code flow requires a code, which is part of the redirectUri's query parameters when the user logged in
        // Wait for the code to be set by a Request to the HTTP Server
        while (code == null) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        logger.info("Got Code");

        httpServer.stop(1);

        // When the code has been retrieved, it can be used in another request to get an access token as well as a refresh token.
        AuthorizationCodeRequest authorizationCodeRequest = spotifyApi.authorizationCode(code).build();
        try {
            AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeRequest.execute();
            logger.info("Got Access & Refresh Token");

            spotifyApi.setAccessToken(authorizationCodeCredentials.getAccessToken());
            spotifyApi.setRefreshToken(authorizationCodeCredentials.getRefreshToken());

            logger.info("Expires in: " + authorizationCodeCredentials.getExpiresIn() + "s");

            saveCredentials();
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            logger.error("Exception while Spotify AuthorizationCodeRequest", e);
            e.printStackTrace();
        }

    }

    /**
     * Tries to retrieve a new access-token with the refresh-token saved in the config.
     * @throws IOException In case of networking issues.
     * @throws ParseException In case of parsing issues.
     * @throws SpotifyWebApiException The Web API returned an error further specified in this exception's root cause.
     */
    private void refreshToken() throws IOException, ParseException, SpotifyWebApiException {
        /*this.spotifyApi = new SpotifyApi.Builder()
                .setClientId(getConfig().getString(getConfig().get("spotify-client-id")))
                .setClientSecret(getConfig().get("spotify-client-secret"))
                .setRefreshToken(credentialsConfig.get("refresh-token"))
                .build();*/

        spotifyApi.setRefreshToken(credentialsConfig.get("refresh-token"));

        AuthorizationCodeRefreshRequest authorizationCodeRefreshRequest = spotifyApi.authorizationCodeRefresh().build();
        AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeRefreshRequest.execute();

        logger.info("Got new Access Token");
        spotifyApi.setAccessToken(authorizationCodeCredentials.getAccessToken());
        logger.info("Expires in: " + authorizationCodeCredentials.getExpiresIn() + "s");
        saveCredentials();
    }

    /**
     * Saves the (access-token and) refresh-token in the spotify-credentials.properties
     */
    public void saveCredentials() {
        logger.info("Saving Credentials ...");
        if (spotifyApi != null) {
            if (spotifyApi.getRefreshToken() != null) {
                credentialsConfig.set("refresh-token", spotifyApi.getRefreshToken());
                credentialsConfig.save();
            }
        }
    }

    private CurrentlyPlayingContext getCurrentlyPlaying() {
        GetInformationAboutUsersCurrentPlaybackRequest getInformationAboutUsersCurrentPlaybackRequest =
                spotifyApi.getInformationAboutUsersCurrentPlayback().build();

        try {
            return getInformationAboutUsersCurrentPlaybackRequest.execute();
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void draw() {
        logger.info("Draw");

        CurrentlyPlayingContext currentlyPlayingContext = getCurrentlyPlaying();

        if (currentlyPlayingContext == null) {
            writeString("Spotify: Error");
        } else {
            String name = currentlyPlayingContext.getItem().getName();
            String artist = "Test";
            boolean isPlaying = currentlyPlayingContext.getIs_playing();
            int progress = currentlyPlayingContext.getProgress_ms();
            int duration = currentlyPlayingContext.getItem().getDurationMs();

            if (name.length() > 21) {
                name = name.substring(0, 18) + "...";
            }
            writeString(name);

            BufferedImage img = getEmptyImage();
            Graphics g = img.getGraphics();

            //g.drawLine(2, 18, getScreenSize().getWidth() - 3, 18);
            //g.drawLine(2, 19, getScreenSize().getWidth() - 3, 19);

            g.drawRect(2, 17, getScreenSize().getWidth() - 5, 3);

            double progressPercent = (double) progress / (double) duration;
            g.fillRect(3, 18, (int) Math.round((getScreenSize().getWidth() - 5) * progressPercent), 2);

            

            writeImage(img);
        }
    }

    private class RedirectHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // Retrieve the code form the request query parameters
            code = queryToMap(exchange.getRequestURI().getQuery()).get("code");

            //Display a simple message in the browser as response
            String response = "You can close this window now.";
            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }

        // Source: https://stackoverflow.com/a/17472462
        private Map<String, String> queryToMap(String query) {
            if(query == null) {
                return null;
            }
            Map<String, String> result = new HashMap<>();
            for (String param : query.split("&")) {
                String[] entry = param.split("=");
                if (entry.length > 1) {
                    result.put(entry[0], entry[1]);
                }else{
                    result.put(entry[0], "");
                }
            }
            return result;
        }
    }
}
