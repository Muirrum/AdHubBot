package net.devosmium.adhubbot;

import sun.plugin2.message.Message;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.internal.json.objects.BanObject;
import sx.blah.discord.handle.impl.events.guild.GuildCreateEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.RequestBuffer;

public class BotUtils {
    //TODO Implement Modlog and Advanced Permissions
    static String RECOG_NAME = "AdHub Bot";
    static String PREFIX = "/";
    static String OWNER_RECOG_NAME = "Dr. Everett Mann";
    
    static IDiscordClient getBuiltDiscordClient(String token) {
        
        return new ClientBuilder().withToken(token).build();
        
    }
    
    static void sendMessage(IChannel channel, String description, String title, MessageReceivedEvent event, Boolean isSuccess){

        // This might look weird but it'll be explained in another page.
        RequestBuffer.request(() -> {
            try{
                EmbedBuilder builder = new EmbedBuilder();

                builder.withAuthorName(BotUtils.RECOG_NAME);
                builder.withAuthorIcon("http://i.imgur.com/PB0Soqj.png");
                builder.withAuthorUrl("http://i.imgur.com/oPvYFj3.png");

                if (isSuccess) {
                    builder.withColor(0, 255, 0);
                } else {
                    builder.withColor(255,0,0);
                }
                builder.withDesc(description);
                builder.withDescription(description);
                builder.withTitle(title);
                builder.withTimestamp(100);

                RequestBuffer.request(() -> event.getChannel().sendMessage(builder.build()));
            } catch (DiscordException e){
                System.err.println("Message could not be sent with error: ");
                e.printStackTrace();
            }
        });
    
    }
    static void muteUser(IUser user, IGuild guild) {
        try {
            IRole mutedRole = guild.getRolesByName(BotUtils.RECOG_NAME + "-Mute").get(0);
            user.addRole(mutedRole);
        } catch(DiscordException e) {
            e.printStackTrace();
        }
    }
    static void unMuteUser(IUser user, IGuild guild) {
        IRole mutedRole = guild.getRolesByName(BotUtils.RECOG_NAME + "-Mute").get(0);
        user.removeRole(mutedRole);
    }
    static void addToModlog(IUser mod, IUser victim, ModlogType type, MessageReceivedEvent event, String reason) {
        switch (type) {
            case BAN:
                sendMessage(event.getGuild().getChannelsByName("mod-log").get(0), "User " + victim + " was" +
                        " banned by " + mod + " with reason " + reason, "CASE: BAN", event, true);
            case KICK:
                sendMessage(event.getGuild().getChannelsByName("mod-log").get(0), "User " + victim + " was" +
                        " kicked by " + mod + " with reason " + reason, "CASE: KICK", event, true);
        }
    }
}
