package net.devosmium.adhubbot;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.GuildCreateEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.impl.obj.Embed;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RequestBuffer;

import java.util.*;

public class CommandHandler {

    // A static map of commands mapping from command string to the functional impl
    private static Map<String, Command> commandMap = new HashMap<>();
    static CharSequence[] bannedWords = {
            "damn",
            "shit"
    };

    // Statically populate the commandMap with the intended functionality
    // Might be better practise to do this from an instantiated objects constructor
    static {

        commandMap.put("testcommand", (event, args) -> {
            BotUtils.sendMessage(event.getChannel(), "This is how messages *should* look!", "Example!", event, true);
        });

        commandMap.put("ping", (event, args) -> {
            BotUtils.sendMessage(event.getChannel(), "pong", "Pong!", event, true);
        });

    }

    @EventSubscriber
    public void onMessageReceived (MessageReceivedEvent event) {
        // This whole FOR loop is the badwords filter.
        for (int i = 0; i < bannedWords.length; i++) {
            System.out.println("I didn't bork the FOR loop!");
            if (event.getMessage().getContent().contains(bannedWords[i])) {
                try {
                    event.getMessage().delete();
                    BotUtils.sendMessage(event.getChannel(), event.getAuthor().mention() + "Watch your language!",
                            "Warning", event, false);
                } catch (MissingPermissionsException e) {
                    System.out.println("Missing Permission DELETE_MESSAGE in guild with ID "
                            + event.getGuild().getStringID() + " and name " + event.getGuild().getName());
                    BotUtils.sendMessage(event.getChannel(), "I'm missing the permission DELETE_MESSAGE in" +
                            " this guild, please ask a moderator to add it to me.", "Error", event, false);
                }
            }
        }
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

        if (commandMap.containsKey(commandStr))
            commandMap.get(commandStr).runCommand(event, argsList);


    }
}