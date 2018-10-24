package vkbot;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import javafx.application.Application;
import org.eclipse.jetty.server.Server;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        Properties properties = readProperties();
        HttpTransportClient client = new HttpTransportClient();
        VkApiClient apiClient = new VkApiClient(client);

    }

    private static GroupActor initVkApi(VkApiClient apiClient, Properties properties) {
        int groupId = Integer.parseInt(properties.getProperty("groupId"));
        if (groupId == 0 || token == null) throw new RuntimeException("Params are not set");
        GroupActor actor = new GroupActor(groupId, token);

        String token = properties.getProperty("token");
        apiClient.groups().getLongPollServer(actor);

        return actor;
    }

    private static Properties readProperties() throws FileNotFoundException {
        InputStream in = Application.class.getClassLoader().getResourceAsStream("config.properties");
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
    }

}
