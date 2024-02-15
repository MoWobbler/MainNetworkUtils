package me.mowobbler.mainbungeeutils.socket;

import me.mowobbler.mainbungeeutils.MainBungeeUtils;
import me.mowobbler.mainbungeeutils.managers.RestartManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketHandler {

    MainBungeeUtils instance;
    RestartManager restarterUtils;

    public SocketHandler(MainBungeeUtils instance, RestartManager restarterUtils) {
        this.instance = instance;
        this.restarterUtils = restarterUtils;
    }

    @SuppressWarnings("InfiniteLoopStatement")
    public void startSocketListener() {
        try (ServerSocket serverSocket = new ServerSocket(32000)) {
            while (true) {
                Socket socket = serverSocket.accept();
                handleConnection(socket);
            }
        } catch (IOException e) {
            instance.getLogger().severe("Error creating server socket: " + e.getMessage());
        }
    }


    private void handleConnection(Socket socket) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            String serverIdentifier = reader.readLine();
            String message = reader.readLine();
            instance.getLogger().info("Received message from Bukkit: " + message);
            if (message.equals("serverstarted")) {
                restarterUtils.setServerLastRequestTime(serverIdentifier);
            }
        } catch (IOException e) {
            instance.getLogger().warning("Error handling connection: " + e.getMessage());
        }
    }
}
