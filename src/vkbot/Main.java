package vkbot;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import javafx.application.Application;
import org.eclipse.jetty.server.Server;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Main {
    private final static String PROPERTY_NAME = "config.properties";
    public static void main(String[] args) throws Exception {
        Properties properties = readProperties();
        HttpTransportClient client = new HttpTransportClient();
        VkApiClient apiClient = new VkApiClient(client);

        GroupActor actor = initVkApi(apiClient, readProperties());
        BotRequestHandler botHandler = new BotRequestHandler(apiClient, actor);

        Server server = new Server(8080);

        server.setHandler(new RequestHandler(botHandler, properties.getProperty("confirmationCode")));

        server.start();
        server.join();

    }

    private static GroupActor initVkApi(VkApiClient apiClient, Properties properties) {
        String token = properties.getProperty("token");
        int groupId = Integer.parseInt(properties.getProperty("groupId"));
        if (groupId == 0 || token == null) throw new RuntimeException("Params are not set");
        GroupActor actor = new GroupActor(groupId, token);

        apiClient.groups().getLongPollServer(actor);

        return actor;
    }

    private static Properties readProperties() throws FileNotFoundException {
        InputStream in = Application.class.getClassLoader().getResourceAsStream(PROPERTY_NAME);
        if (in == null) {
            throw new FileNotFoundException("dam son");
        }
        try {
            Properties properties = new Properties();
            properties.load(in);
            return properties;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
