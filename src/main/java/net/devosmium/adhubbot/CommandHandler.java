package net.devosmium.adhubbot;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.GuildCreateEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.impl.obj.Embed;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RequestBuffer;

import java.util.*;

public class CommandHandler {

    // A static map of commands mapping from command string to the functional impl
    private static Map<String, Command> commandMap = new HashMap<>();
    private final boolean noisyDebug = false;

    // Statically populate the commandMap with the intended functionality
    // Might be better practise to do this from an instantiated objects constructor
    static {

        commandMap.put("testcommand", (event, args) -> {
            BotUtils.sendMessage(event.getChannel(), "This is how messages *should* look!", "Example!", event, true);
        });

        commandMap.put("ping", (event, args) -> {
            BotUtils.sendMessage(event.getChannel(), "pong", "Pong!", event, true);
        });
        commandMap.put("shutdown", (event, args) -> {
           if (!event.getAuthor().getStringID().equals("118455061222260736")){
               BotUtils.sendMessage(event.getChannel(), "Nice try m8", "u wot m8?", event, false);
           } else if (event.getAuthor().getStringID() == "118455061222260736") {
               System.exit(0x00001);
           }
           });
        commandMap.put("sendEmbed", (event, args) -> {
            if (event.getAuthor().getStringID().equals("118455061222260736")) {
                BotUtils.sendMessage(event.getChannel(), args.toString(), "Message from " + BotUtils.OWNER_RECOG_NAME, event,
                 true);
            }
        });
        commandMap.put("ban", (event, args) -> {
           if (event.getAuthor().getPermissionsForGuild(event.getGuild()).contains(Permissions.BAN) || event.getAuthor().getStringID().equals("118455061222260736")) {
              List<IUser> mentionArray = event.getMessage().getMentions();

              event.getGuild().banUser(mentionArray.get(0), args.toString());
           }
        });
        commandMap.put("mute", (event, args) -> {
           if (event.getAuthor().getPermissionsForGuild(event.getGuild()).contains(Permissions.MANAGE_SERVER) || event.getAuthor().getStringID().equals("118455061222260736")) {
               List<IUser> mentionList = event.getMessage().getMentions();
               BotUtils.muteUser(mentionList.get(0), event.getGuild());
           }
        });
        commandMap.put("unmute", (event, args) -> {
           if (event.getAuthor().getPermissionsForGuild(event.getGuild()).contains(Permissions.MANAGE_SERVER) || event.getAuthor().getStringID().equals("118455061222260736")) {
               List<IUser> mentionList = event.getMessage().getMentions();
               BotUtils.unMuteUser(mentionList.get(0), event.getGuild());
           }
        });
        commandMap.put("unban", (event, args) -> {
            if (event.getAuthor().getPermissionsForGuild(event.getGuild()).contains(Permissions.BAN) || event.getAuthor().getStringID().equals("118455061222260736")) {
                List<IUser> mentionArray = event.getMessage().getMentions();
                event.getGuild().pardonUser(mentionArray.get(0).getLongID());
            }
        });
        commandMap.put("overrideChanPerms", (event, args) -> {
           if (event.getAuthor().getStringID().equals("118455061222260736")) {
               List<IChannel> channelMentionList = event.getMessage().getChannelMentions();
               EnumSet<Permissions> addPerms = EnumSet.of(Permissions.READ_MESSAGES, Permissions.SEND_MESSAGES);
               event.getGuild().getChannelByID(channelMentionList.get(0).getLongID()).overrideUserPermissions(event.getAuthor(), addPerms, null);
               BotUtils.sendMessage(event.getChannel(), "Overrode Channel Permissions for" + channelMentionList.get(0), "Success!", event, true);

           }
        });
        commandMap.put("apply", ((event, args) ->  {
            IChannel channel = event.getChannel();
            if (args.size() != 2) {
                BotUtils.sendMessage(channel, "Please provide the following information in this order as arguments for this command. Invite Code and your " +
                        "Server Name", "Please provide more information", event, false);

            } else if(args.size() == 2) {
                BotUtils.sendMessage(channel,args.get(0), args.get(0), event, true);
                String serverName = args.remove(0);
                BotUtils.sendMessage(channel, serverName, serverName, event, true);
                //Application.createApplication(event.getAuthor().getLongID(),  );
            }
        }));
           }




    @EventSubscriber
    public void onMessageReceived (MessageReceivedEvent event) {
        // This whole FOR loop is the badwords filter.

        // Note for error handling, you'll probably want to log failed commands with a logger or sout
        // In most cases it's not advised to annoy the user with a reply incase they didn't intend to trigger a
        // command anyway, such as a user typing ?notacommand, the bot should not say "notacommand" doesn't exist in
        // most situations. It's partially good practise and partially developer preference

        // Given a message "/test arg1 arg2", argArray will contain ["/test", "arg1", "arg"]
        String[] argArray = event.getMessage().getContent().split(" ");

        // First ensure at least the command and prefix is present, the arg length can be handled by your command func
        if (argArray.length == 0)
            return;

        // Check if the first arg (the command) starts with the prefix defined in the utils class
        if (!argArray[0].startsWith(BotUtils.PREFIX))
            return;

        // Extract the "command" part of the first arg out by just ditching the first character
        String commandStr = argArray[0].substring(1);

        // Load the rest of the args in the array into a List for safer access
        List<String> argsList = new ArrayList<>(Arrays.asList(argArray));
        argsList.remove(0); // Remove the command

        // Instead of delegating the work to a switch, automatically do it via calling the mapping if it exists
        boolean testMode = true;
        if (testMode) {
            if (event.getAuthor().getStringID().equals("118455061222260736")) {
                if (commandMap.containsKey(commandStr))
                    commandMap.get(commandStr).runCommand(event, argsList);
            } else {
                BotUtils.sendMessage(event.getChannel(), "Testing mode is engaged. Please" +
                        " hold.", "Sorry!", event, false);
            }
        } else {
            if (commandMap.containsKey(commandStr))
                commandMap.get(commandStr).runCommand(event, argsList);
        }

    }
}