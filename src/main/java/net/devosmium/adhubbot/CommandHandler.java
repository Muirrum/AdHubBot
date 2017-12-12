package net.devosmium.adhubbot;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.IShard;
import sx.blah.discord.api.events.EventDispatcher;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.api.internal.json.objects.InviteObject;
import sx.blah.discord.handle.impl.events.guild.GuildCreateEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.impl.obj.Embed;
import sx.blah.discord.handle.impl.obj.Invite;
import sx.blah.discord.handle.obj.*;
import sx.blah.discord.modules.ModuleLoader;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.Image;
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
            if (args.size() != 1) {
                BotUtils.sendMessage(event.getChannel(), "Please provide your invite code and only your invite code as the argument",
                        "Please fix your arguments", event, false);
            } else if (args.size() == 1) {
                IDiscordClient client = new IDiscordClient() {
                    @Override
                    public EventDispatcher getDispatcher() {
                        return null;
                    }

                    @Override
                    public ModuleLoader getModuleLoader() {
                        return null;
                    }

                    @Override
                    public List<IShard> getShards() {
                        return null;
                    }

                    @Override
                    public int getShardCount() {
                        return 0;
                    }

                    @Override
                    public String getToken() {
                        return null;
                    }

                    @Override
                    public void login() {

                    }

                    @Override
                    public void logout() {

                    }

                    @Override
                    public void changeUsername(String s) {

                    }

                    @Override
                    public void changeAvatar(Image image) {

                    }

                    @Override
                    public void changePlayingText(String s) {

                    }

                    @Override
                    public void online(String s) {

                    }

                    @Override
                    public void online() {

                    }

                    @Override
                    public void idle(String s) {

                    }

                    @Override
                    public void idle() {

                    }

                    @Override
                    public void streaming(String s, String s1) {

                    }

                    @Override
                    public void dnd(String s) {

                    }

                    @Override
                    public void dnd() {

                    }

                    @Override
                    public void invisible() {

                    }

                    @Override
                    public void mute(IGuild iGuild, boolean b) {

                    }

                    @Override
                    public void deafen(IGuild iGuild, boolean b) {

                    }

                    @Override
                    public boolean isReady() {
                        return false;
                    }

                    @Override
                    public boolean isLoggedIn() {
                        return false;
                    }

                    @Override
                    public IUser getOurUser() {
                        return null;
                    }

                    @Override
                    public List<IChannel> getChannels(boolean b) {
                        return null;
                    }

                    @Override
                    public List<IChannel> getChannels() {
                        return null;
                    }

                    @Override
                    public IChannel getChannelByID(long l) {
                        return null;
                    }

                    @Override
                    public List<IVoiceChannel> getVoiceChannels() {
                        return null;
                    }

                    @Override
                    public IVoiceChannel getVoiceChannelByID(long l) {
                        return null;
                    }

                    @Override
                    public List<IGuild> getGuilds() {
                        return null;
                    }

                    @Override
                    public IGuild getGuildByID(long l) {
                        return null;
                    }

                    @Override
                    public List<IUser> getUsers() {
                        return null;
                    }

                    @Override
                    public IUser getUserByID(long l) {
                        return null;
                    }

                    @Override
                    public IUser fetchUser(long l) {
                        return null;
                    }

                    @Override
                    public List<IUser> getUsersByName(String s) {
                        return null;
                    }

                    @Override
                    public List<IUser> getUsersByName(String s, boolean b) {
                        return null;
                    }

                    @Override
                    public List<IRole> getRoles() {
                        return null;
                    }

                    @Override
                    public IRole getRoleByID(long l) {
                        return null;
                    }

                    @Override
                    public List<IMessage> getMessages(boolean b) {
                        return null;
                    }

                    @Override
                    public List<IMessage> getMessages() {
                        return null;
                    }

                    @Override
                    public IMessage getMessageByID(long l) {
                        return null;
                    }

                    @Override
                    public IPrivateChannel getOrCreatePMChannel(IUser iUser) {
                        return null;
                    }

                    @Override
                    public IInvite getInviteForCode(String s) {
                        return null;
                    }

                    @Override
                    public List<IRegion> getRegions() {
                        return null;
                    }

                    @Override
                    public IRegion getRegionByID(String s) {
                        return null;
                    }

                    @Override
                    public List<IVoiceChannel> getConnectedVoiceChannels() {
                        return null;
                    }

                    @Override
                    public String getApplicationDescription() {
                        return null;
                    }

                    @Override
                    public String getApplicationIconURL() {
                        return null;
                    }

                    @Override
                    public String getApplicationClientID() {
                        return null;
                    }

                    @Override
                    public String getApplicationName() {
                        return null;
                    }

                    @Override
                    public IUser getApplicationOwner() {
                        return null;
                    }
                };
                IInvite serverInvite = client.getInviteForCode(args.get(0));
                IChannel channel = event.getChannel();
                BotUtils.sendMessage(channel, serverInvite.getGuild().getName(), "Name", event, true);
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