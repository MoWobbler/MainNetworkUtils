package me.mowobbler.mainbungeeutils.managers;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.HashMap;

public class MessageManager {

    private final HashMap<ProxiedPlayer, MessageContext> lastMessages;

    public MessageManager() {
        this.lastMessages = new HashMap<>();
    }


    public boolean isValidSender(CommandSender sender) {
        return sender instanceof ProxiedPlayer;
    }


    public boolean isValidReceiver(String unvalidatedReceiver) {
        ProxiedPlayer receiver = null;
        for (ProxiedPlayer playerCheck : ProxyServer.getInstance().getPlayers()) {
            if (playerCheck.getName().equalsIgnoreCase(unvalidatedReceiver)) {
                receiver = playerCheck;
                break;
            }
        }

        return receiver != null;
    }


    public String buildMessage(String[] args, int index) {
        StringBuilder message = new StringBuilder();
        for (int i = index; i < args.length; i++) {
            message.append(args[i]);
            if (i < args.length - 1) {
                message.append(" ");
            }
        }
        return message.toString();
    }


    public void setLastPlayerMessaged(ProxiedPlayer sender, ProxiedPlayer receiver) {
        MessageContext context = lastMessages.computeIfAbsent(sender, k -> new MessageContext());
        context.setLastPlayerMessaged(receiver);
    }


    public ProxiedPlayer getLastPlayerMessaged(ProxiedPlayer sender) {
        MessageContext context = lastMessages.get(sender);
        return (context != null) ? context.getLastPlayerMessaged() : null;
    }


    public void setLastPlayerMessagedBy(ProxiedPlayer receiver, ProxiedPlayer sender) {
        MessageContext context = lastMessages.computeIfAbsent(receiver, k -> new MessageContext());
        context.setLastPlayerMessagedBy(sender);
    }


    public ProxiedPlayer getLastPlayerMessagedBy(ProxiedPlayer receiver) {
        MessageContext context = lastMessages.get(receiver);
        return (context != null) ? context.getLastPlayerMessagedBy() : null;
    }



    private static class MessageContext {
        private ProxiedPlayer lastPlayerMessaged;
        private ProxiedPlayer lastPlayerMessagedBy;

        public ProxiedPlayer getLastPlayerMessaged() {
            return lastPlayerMessaged;
        }

        public void setLastPlayerMessaged(ProxiedPlayer lastPlayerMessaged) {
            this.lastPlayerMessaged = lastPlayerMessaged;
        }

        public ProxiedPlayer getLastPlayerMessagedBy() {
            return lastPlayerMessagedBy;
        }

        public void setLastPlayerMessagedBy(ProxiedPlayer lastPlayerMessagedBy) {
            this.lastPlayerMessagedBy = lastPlayerMessagedBy;
        }
    }
}
